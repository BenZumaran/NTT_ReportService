package com.nttdata.report_service.service.report;


import com.nttdata.report_service.model.CommissionReport;
import com.nttdata.report_service.model.DailyBalanceAvgReport;
import com.nttdata.report_service.model.GeneralProductReport;
import com.nttdata.report_service.model.TotalProductsClientReport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ReportService {

    Mono<CommissionReport> getCommissionReportByProductId(String id, String from, String to);

    Mono<DailyBalanceAvgReport> getResumeDailyBalanceAvgReportByClientDocument(String document);

    Mono<GeneralProductReport> getResumeProductGeneralReport(String id);

    Mono<TotalProductsClientReport> getResumeAllClientProductsReport(String id);


}
