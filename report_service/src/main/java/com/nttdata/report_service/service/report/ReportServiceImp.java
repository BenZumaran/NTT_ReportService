package com.nttdata.report_service.service.report;

import com.nttdata.report_service.dto.account.AccountResponseDTO;
import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.mapper.*;
import com.nttdata.report_service.model.CommissionReport;
import com.nttdata.report_service.model.DailyBalanceAvgReport;
import com.nttdata.report_service.model.GeneralProductReport;
import com.nttdata.report_service.model.TotalProductsClientReport;
import com.nttdata.report_service.service.account.AccountService;
import com.nttdata.report_service.service.credit.CreditService;
import com.nttdata.report_service.service.customer.CustomerService;
import com.nttdata.report_service.service.transaction.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;

@Service
public class ReportServiceImp implements ReportService{

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
    public Mono<CommissionReport> getCommissionReportByProductId(String id, String from, String to) {

        //Se obtiene el Flux<TransactionResponseDTO>
        return transactionService.fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(
                "charge",id, LocalDateTime.parse(from), LocalDateTime.parse(to))
                //Si el objeto está vacío, se envía error personalizado
                .switchIfEmpty(Mono.error(new Error("Not transactions find")))
                //Se colecta la lista y transforma en ComissionReport
                .collectList().map(ReportMapper::getComissionReportFromTransactionResponseDtoList);
    }

    @Override
    public Mono<DailyBalanceAvgReport> getResumeDailyBalanceAvgReportByClientDocument(String document) {

        //Se obtiene la fecha actual
        LocalDateTime currentDate = LocalDateTime.now();

        //Se obtiene Primer día del mes
        LocalDateTime firstCurrentMonthDate = LocalDateTime.of(currentDate.getYear(),currentDate.getMonthValue(),1,0,0,0);

        //Se obtiene Flux<TransactionResponseDTO>
        return transactionService.fetchGetTransactionsByClientDocumentBetweenTimeDate(document,  firstCurrentMonthDate, currentDate)
                //Se colecta lista y convierte a DailyBalanceAvgReport
                .collectList().map(DailyBalanceObjectMapper::getDailyBalanceAvgReportFromTransactionResponseDtoList);
    }

    @Override
    public Mono<GeneralProductReport> getResumeProductGeneralReport(String id) {
        return accountService.fetchGetAccountById(id)
                .map(ProductMapper::getProductDetailFromExternalResponse)
                .onErrorResume( error ->
                        creditService.fetchGetCreditById(id)
                                .map(ProductMapper::getProductDetailFromExternalResponse)
                                .switchIfEmpty(Mono.error(new Error("Not product for the id "+ id +" found.")))
                ).zipWhen(productDetail ->
                        transactionService.fetchGetTransactionsByProductId(productDetail.getId())
                                .map(TransactionMapper::getTransactionFromTransactionResponseDto)
                                .onErrorResume(error->Flux.empty())
                                .collectList())
                .map(ReportMapper::getGeneralProductReportFromTransactionListAndProductDetail);
    }

    @Override
    public Mono<TotalProductsClientReport> getResumeAllClientProductsReport(String id) {
        return customerService.fetchGetCustomerById(id)
                .map(ClientMapper::getClientFromCustomerResponseDto)
                .map(ReportMapper::getTotalProductsClientReportFromClient)
                .flatMap(report -> accountService
                        .fetchGetAccountsByHolderDocument(report.getClient().getDocument())
                        .map(ProductMapper::getProductDetailFromExternalResponse)
                        .collectList()
                        .map(productDetails ->  ReportMapper.updateTotalProductsClientReportFromProduct(
                                report, productDetails))
                        .onErrorResume(error -> {
                            log.error( "Not accounts found for client with document {}, error message: {}",
                                    report.getClient().getDocument(), error.getMessage());
                            return Mono.just(report);
                        }))
                .flatMap(report -> creditService
                        .fetchGetCredits()
                        .filter(creditResponseDTO -> creditResponseDTO
                                .getCustomerId().equals(report.getClient().getId()))
                        .map(ProductMapper::getProductDetailFromExternalResponse)
                        .collectList()
                        .map(productDetails ->  ReportMapper.updateTotalProductsClientReportFromProduct(
                                report, productDetails))
                        //.onErrorReturn(report))
                        .onErrorResume(error ->{
                            log.error( "Not credits found for client with id {}, error message: {}",
                                    report.getClient().getId(), error.getMessage());
                            return Mono.just(report);
                        }))
                .doOnError(error->{
                    log.error("Error: {} in -> getResumeAllClientProductsReport",error.getMessage());
                });

    }
}
