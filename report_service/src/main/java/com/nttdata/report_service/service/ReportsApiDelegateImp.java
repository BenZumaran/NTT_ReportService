package com.nttdata.report_service.service;

import com.nttdata.report_service.api.ReportsApiDelegate;
import com.nttdata.report_service.model.*;
import com.nttdata.report_service.service.report.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ReportsApiDelegateImp implements ReportsApiDelegate {

    @Autowired
    ReportService reportService;

    @Override
    public Mono<ResponseEntity<ComissionReport>> reportsCommissionProductIdGet(String id,
                                                                       String from,
                                                                       String to,
                                                                       ServerWebExchange exchange){
        //Se obtiene el objeto Mono<Comission> y se convierte en ResponseEntity
        return reportService.getCommissionReportByProductId(id,from,to)
                .map(
                        comissionReport ->
                                new ResponseEntity<>(comissionReport, HttpStatus.OK)
                )
                //Se maneja el error y se devuelve el mensaje por logger
                .onErrorResume(error->{
                    log.error("Error {} -> reportsComissionProductIdGet.", error.getMessage());
                    return Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                });
    }

    @Override
    public Mono<ResponseEntity<DailyBalanceAvgReport>> reportsResumeDailyBalanceAvgClientDocumentGet(String document,
                                                                                               ServerWebExchange exchange){
        //Se obtiene el objeto Mono<DailyBalanceAvgReport> y se convierte en ResponseEntity
        return reportService.getResumeDailyBalanceAvgReportByClientDocument(document)
                .map(dailyBalanceAvgReport -> new ResponseEntity<>(dailyBalanceAvgReport,HttpStatus.OK))
                .doOnSuccess(
                        nousing -> log.info("Report created correctly for Client with document {}", document))
                //Se maneja el rror y devuelve el mensaje por logger
                .onErrorResume(error->{
                    log.error("Error {} -> reportsResumeDailyBalanceAvgClientIdGet.", error.getMessage());
                    return Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                });
    }



}
