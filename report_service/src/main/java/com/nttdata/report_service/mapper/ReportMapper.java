package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.*;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class ReportMapper {

    //Se convierte List<TransactionResponseDTO> a ComissionReport
    public static CommissionReport getComissionReportFromTransactionResponseDtoList(List<TransactionResponseDTO> transactionResponseDTOS){

        CommissionReport comissionReport = new CommissionReport();
        comissionReport.setReportDate(OffsetDateTime.now());

        comissionReport.setProduct(
                //Se convierte TransactionProductDto a Product del primer objeto
                ProductMapper.getProductFromTransactionProductDto(
                        transactionResponseDTOS.get(0).getProduct()
                ));

        //Se obtiene El total reduciendo la lista a double sum
        comissionReport.setTotalAmount(BigDecimal.valueOf(transactionResponseDTOS.stream()
                .mapToDouble(TransactionResponseDTO::getAmount)
                .reduce(0.0, Double::sum)));

        //Se valida
        comissionReport.setCommissionsDetail(transactionResponseDTOS.stream()
                .map(CommissionDetailMapper::getCommissionDetailFromTransactionResponseDto)
                .collect(Collectors.toList()));

        return comissionReport;
    }

    public static GeneralProductReport getGeneralProductReportFromTransactionListAndProductDetail(
            Tuple2<ProductDetail, List<Transaction>  > tuple2){
        GeneralProductReport generalProductReport = new GeneralProductReport();

        generalProductReport.setReportDate(OffsetDateTime.now());
        generalProductReport.setProduct(tuple2.getT1());
        if (!tuple2.getT2().isEmpty())
            generalProductReport.setTransactions(tuple2.getT2());

        return  generalProductReport;

    }
    public static TotalProductsClientReport getTotalProductsClientReportFromClient(Client client){
        TotalProductsClientReport totalProductsClientReport = new TotalProductsClientReport();
        totalProductsClientReport.setClient(client);
        totalProductsClientReport.setReportDate(OffsetDateTime.now());

        TotalProductsClientReportReportDetail reportDetail = new TotalProductsClientReportReportDetail();
        TotalProductsClientReportReportDetailAccounts reportDetailAccounts = new TotalProductsClientReportReportDetailAccounts();
        TotalProductsClientReportReportDetailCredits reportDetailCredits = new TotalProductsClientReportReportDetailCredits();

        reportDetail.setAccounts(reportDetailAccounts);
        reportDetail.setCredits(reportDetailCredits);
        totalProductsClientReport.setReportDetail(reportDetail);
        totalProductsClientReport.setTotalProducts(BigDecimal.ZERO);
        return totalProductsClientReport;
    }

    public static TotalProductsClientReport updateTotalProductsClientReportFromProduct(
            TotalProductsClientReport totalProductsClientReport, List<ProductDetail> productList){
        totalProductsClientReport.setTotalProducts(
                BigDecimal.valueOf(totalProductsClientReport.getTotalProducts().intValue()+productList.size()));
        if (productList.get(0).getType().getValue().equals("savings_account") ||
                productList.get(0).getType().getValue().equals("checking_account") ||
                productList.get(0).getType().getValue().equals("fixed_term_account") ){
            totalProductsClientReport.getReportDetail().getAccounts().setProducts(productList);
            totalProductsClientReport.getReportDetail().getAccounts().setAmount(BigDecimal.valueOf(productList.size()));
        }
        if (productList.get(0).getType().getValue().equals("personal_credit") ||
                productList.get(0).getType().getValue().equals("business_credit") ||
                productList.get(0).getType().getValue().equals("credit_card") ){
            totalProductsClientReport.getReportDetail().getCredits().setProducts(productList);
            totalProductsClientReport.getReportDetail().getCredits().setAmount(BigDecimal.valueOf(productList.size()));
        }
        return totalProductsClientReport;
    }


}
