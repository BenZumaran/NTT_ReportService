package com.nttdata.report_service.service.report;

import com.nttdata.report_service.mapper.*;
import com.nttdata.report_service.model.CommissionReport;
import com.nttdata.report_service.model.DailyBalanceAvgReport;
import com.nttdata.report_service.model.GeneralProductReport;
import com.nttdata.report_service.model.TotalProductsClientReport;
import com.nttdata.report_service.service.account.AccountService;
import com.nttdata.report_service.service.credit.CreditService;
import com.nttdata.report_service.service.customer.CustomerService;
import com.nttdata.report_service.service.transaction.TransactionService;
import com.nttdata.report_service.util.exceptions.CreditNotFoundException;
import com.nttdata.report_service.util.exceptions.ReportNotFoundException;
import com.nttdata.report_service.util.exceptions.TransactionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
public class ReportServiceImp implements ReportService {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    CreditService creditService;

    @Autowired
    CustomerService customerService;

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImp.class);


    @Override
    public Mono<CommissionReport> getCommissionReportByProductId(String id, String from, String to) throws DateTimeParseException {

        //Se obtiene el Flux<TransactionResponseDTO>
        return transactionService.fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(
                        "charge", id, LocalDateTime.parse(from), LocalDateTime.parse(to))
                //Si el objeto está vacío, se envía error personalizado
                .switchIfEmpty(Mono.error(new TransactionNotFoundException()))
                //Se colecta la lista y transforma en ComissionReport
                .collectList().map(ReportMapper::getComissionReportFromTransactionResponseDtoList);
    }

    @Override
    public Mono<DailyBalanceAvgReport> getResumeDailyBalanceAvgReportByClientDocument(String document) throws TransactionNotFoundException {

        //Se obtiene la fecha actual
        LocalDateTime currentDate = LocalDateTime.now();

        //Se obtiene Primer día del mes
        LocalDateTime firstCurrentMonthDate = LocalDateTime.of(currentDate.getYear(), currentDate.getMonthValue(), 1, 0, 0, 0);

        //Se obtiene Flux<TransactionResponseDTO>
        return transactionService.fetchGetTransactionsByClientDocumentBetweenTimeDate(document, firstCurrentMonthDate, currentDate)
                .switchIfEmpty(Mono.error(new TransactionNotFoundException()))
                //Se colecta lista y convierte a DailyBalanceAvgReport
                .collectList().map(DailyBalanceObjectMapper::getDailyBalanceAvgReportFromTransactionResponseDtoList);
    }

    @Override
    public Mono<GeneralProductReport> getResumeProductGeneralReport(String id) {
        return accountService.fetchGetAccountById(id)
                .map(ProductMapper::getProductDetailFromExternalResponse)
                .onErrorResume(error ->
                        creditService.fetchGetCreditById(id)
                                .map(ProductMapper::getProductDetailFromExternalResponse)
                ).zipWhen(productDetail ->
                        transactionService.fetchGetTransactionsByProductId(productDetail.getId())
                                .map(TransactionMapper::getTransactionFromTransactionResponseDto)
                                .onErrorResume(error -> Flux.empty())
                                .collectList())
                .map(ReportMapper::getGeneralProductReportFromTransactionListAndProductDetail)
                .doOnError(throwable -> throwable instanceof CreditNotFoundException, error -> {
                    ;
                    log.error("Error: {} in -> getResumeProductGeneralReport", error.getMessage());
                    throw new ReportNotFoundException();
                });
    }

    @Override
    public Mono<TotalProductsClientReport> getResumeAllClientProductsReport(String id) throws ReportNotFoundException {
        return customerService.fetchGetCustomerById(id)
                .map(ClientMapper::getClientFromCustomerResponseDto)
                .map(ReportMapper::getTotalProductsClientReportFromClient)
                .flatMap(report -> accountService
                        .fetchGetAccountsByHolderDocument(report.getClient().getDocument())
                        .map(ProductMapper::getProductDetailFromExternalResponse)
                        .collectList()
                        .map(productDetails -> ReportMapper.updateTotalProductsClientReportFromProduct(
                                report, productDetails))
                        .onErrorResume(error -> {
                            log.error("Not accounts found for client with document {}, error message: {}",
                                    report.getClient().getDocument(), error.getMessage());
                            return Mono.just(report);
                        }))
                .flatMap(report -> creditService
                        .fetchGetCredits()
                        .filter(creditResponseDTO -> creditResponseDTO
                                .getCustomerId().equals(report.getClient().getId()))
                        .map(ProductMapper::getProductDetailFromExternalResponse)
                        .collectList()
                        .map(productDetails -> ReportMapper.updateTotalProductsClientReportFromProduct(
                                report, productDetails))
                        //.onErrorReturn(report))
                        .onErrorResume(error -> {
                            log.error("Not credits found for client with id {}, error message: {}",
                                    report.getClient().getId(), error.getMessage());
                            return Mono.just(report);
                        }))
                .doOnError(error -> {
                    log.error("Error: {} in -> getResumeAllClientProductsReport", error.getMessage());
                });

    }
}
