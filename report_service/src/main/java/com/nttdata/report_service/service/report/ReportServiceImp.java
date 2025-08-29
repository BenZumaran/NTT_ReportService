package com.nttdata.report_service.service.report;

import com.nttdata.report_service.mapper.DailyBalanceObjectMapper;
import com.nttdata.report_service.mapper.ReportMapper;
import com.nttdata.report_service.model.ComissionReport;
import com.nttdata.report_service.model.DailyBalanceAvgReport;
import com.nttdata.report_service.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;

@Service
public class ReportServiceImp implements ReportService{

    @Autowired
    TransactionService transactionService;

    @Override
    public Mono<ComissionReport> getCommissionReportByProductId(String id, String from, String to) {

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
}
