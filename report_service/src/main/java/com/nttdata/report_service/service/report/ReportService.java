package com.nttdata.report_service.service.report;


import com.nttdata.report_service.model.ComissionReport;
import com.nttdata.report_service.model.DailyBalanceAvgReport;
import reactor.core.publisher.Mono;

public interface ReportService {

    Mono<ComissionReport> getCommissionReportByProductId(String id, String from, String to);

    Mono<DailyBalanceAvgReport> getResumeDailyBalanceAvgReportByClientDocument(String document);

}
