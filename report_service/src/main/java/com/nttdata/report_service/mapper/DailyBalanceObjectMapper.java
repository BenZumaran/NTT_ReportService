package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DailyBalanceObjectMapper {

    public static DailyBalanceAvgReport getDailyBalanceAvgReportFromTransactionResponseDtoList(List<TransactionResponseDTO> transactionResponseDTOList){
        DailyBalanceAvgReport dailyBalanceAvgReport = new DailyBalanceAvgReport();
        DailyBalanceAvgReportReportDetail dailyBalanceAvgReportReportDetail = getDailyBalanceAvgReportReportDetailFromTransactionResponseDtoList(transactionResponseDTOList);

        dailyBalanceAvgReport.setReportDetail(dailyBalanceAvgReportReportDetail);

        double dailyAccountsTotal = dailyBalanceAvgReportReportDetail.getAccounts() != null
                ? dailyBalanceAvgReportReportDetail.getAccounts().getTotalAccounts().doubleValue()
                : 0,
                dailyCreditsTotal = dailyBalanceAvgReportReportDetail.getCredits() != null
                        ? dailyBalanceAvgReportReportDetail.getCredits().getTotalCredits().doubleValue()
                        : 0;

        double dailyAccountsAvg = dailyBalanceAvgReportReportDetail.getAccounts() != null
                ? dailyBalanceAvgReportReportDetail.getAccounts().getAccountsAverage().doubleValue()
                : 0,
                dailyCreditsAvg = dailyBalanceAvgReportReportDetail.getCredits()!= null
                        ? dailyBalanceAvgReportReportDetail.getCredits().getCreditsAverage().doubleValue()
                        : 0;


        dailyBalanceAvgReport.setTotalProducts(
                BigDecimal.valueOf(dailyAccountsTotal + dailyCreditsTotal));

        dailyBalanceAvgReport.setTotalAverage(BigDecimal.valueOf(
                ( dailyAccountsAvg + dailyCreditsAvg ) / 2 ));

        dailyBalanceAvgReport.setClient(
                ClientMapper.getClientFromTransactionClientDto(transactionResponseDTOList.get(0).getClient()));

        dailyBalanceAvgReport.setReportDate(OffsetDateTime.now());

        return dailyBalanceAvgReport;

    }

    public static DailyBalanceAvgReportReportDetail getDailyBalanceAvgReportReportDetailFromTransactionResponseDtoList(List<TransactionResponseDTO> transactionResponseDTOList){
        DailyBalanceAvgReportReportDetail dailyBalanceAvgReportReportDetail = new DailyBalanceAvgReportReportDetail();

        Predicate<TransactionResponseDTO> isAccount = transactionResponseDTO ->
                transactionResponseDTO.getProduct().getType().equals("savings_account") ||
                        transactionResponseDTO.getProduct().getType().equals("checking_account") ||
                        transactionResponseDTO.getProduct().getType().equals("fixed_term_account");

        Predicate<TransactionResponseDTO> isCredit = transactionResponseDTO ->
                transactionResponseDTO.getProduct().getType().equals("personal_credit") ||
                        transactionResponseDTO.getProduct().getType().equals("business_credit") ||
                        transactionResponseDTO.getProduct().getType().equals("credit_card");

        List<TransactionResponseDTO> transactionAccountsResponseDTOS = transactionResponseDTOList.stream().filter(isAccount).collect(Collectors.toList());
        List<TransactionResponseDTO> transactionCreditsResponseDTOS = transactionResponseDTOList.stream().filter(isCredit).collect(Collectors.toList());

        DailyBalanceAvgReportReportDetailAccounts transactionAccounts = null;
        DailyBalanceAvgReportReportDetailCredits transactionCredits = null;

        if (!transactionAccountsResponseDTOS.isEmpty())
            transactionAccounts = getDailyBalanceAvgReportReportDetailAccountsFromTransactionResponseDtoList(
                    transactionAccountsResponseDTOS);
        System.out.println(transactionCreditsResponseDTOS);
        if (!transactionCreditsResponseDTOS.isEmpty())
            transactionCredits = getDailyBalanceAvgReportReportDetailCreditsFromTransactionResponseDtoList(
                        transactionCreditsResponseDTOS);

        dailyBalanceAvgReportReportDetail.setAccounts(transactionAccounts);
        dailyBalanceAvgReportReportDetail.setCredits(transactionCredits);

        return  dailyBalanceAvgReportReportDetail;

    }

    public static DailyBalanceAvgReportReportDetailAccounts getDailyBalanceAvgReportReportDetailAccountsFromTransactionResponseDtoList(List<TransactionResponseDTO> transactionResponseDTOList){
        DailyBalanceAvgReportReportDetailAccounts dailyBalanceAvgReportReportDetailAccounts = new DailyBalanceAvgReportReportDetailAccounts();

        List<ProductDailyBalanceAvg> productDailyBalanceAvgList = new ArrayList<>();
        transactionResponseDTOList.stream()
                .map(TransactionResponseDTO::getProduct)
                .map(TransactionProductDTO::getId)
                .distinct().forEach(
                ids -> {
                    productDailyBalanceAvgList.add(
                            getProductDailyBalanceAvgFromTransactionResponseDtoList(
                                    transactionResponseDTOList.stream().filter(
                                                    transactionResponseDTO1 -> transactionResponseDTO1
                                                            .getProduct().getId().equals(
                                                                    ids))
                                            .collect(Collectors.toList())));
                });
        dailyBalanceAvgReportReportDetailAccounts.setProducts(productDailyBalanceAvgList);

        dailyBalanceAvgReportReportDetailAccounts.setTotalAccounts(
                BigDecimal.valueOf (productDailyBalanceAvgList.size()));

        dailyBalanceAvgReportReportDetailAccounts.setAccountsAverage(BigDecimal.valueOf(
                productDailyBalanceAvgList.stream().map(
                        ProductDailyBalanceAvg::getBalanceAvg)
                        .mapToDouble(BigDecimal::doubleValue)
                        .average().getAsDouble()));

        return  dailyBalanceAvgReportReportDetailAccounts;

    }

    public static DailyBalanceAvgReportReportDetailCredits getDailyBalanceAvgReportReportDetailCreditsFromTransactionResponseDtoList(List<TransactionResponseDTO> transactionResponseDTOList){
        DailyBalanceAvgReportReportDetailCredits dailyBalanceAvgReportReportDetailCredits = new DailyBalanceAvgReportReportDetailCredits();

        List<ProductDailyBalanceAvg> productDailyBalanceAvgList = new ArrayList<>();
        transactionResponseDTOList.stream()
                .map(TransactionResponseDTO::getProduct)
                .map(TransactionProductDTO::getId)
                .distinct().forEach(
                ids -> {
                    productDailyBalanceAvgList.add(
                            getProductDailyBalanceAvgFromTransactionResponseDtoList(
                                    transactionResponseDTOList.stream().filter(
                                                    transactionResponseDTO1 -> transactionResponseDTO1
                                                            .getProduct().getId().equals(
                                                                    ids))
                                            .collect(Collectors.toList())));
                });
        dailyBalanceAvgReportReportDetailCredits.setProducts(productDailyBalanceAvgList);

        dailyBalanceAvgReportReportDetailCredits.setTotalCredits(
                BigDecimal.valueOf (productDailyBalanceAvgList.size()));

        dailyBalanceAvgReportReportDetailCredits.setCreditsAverage(BigDecimal.valueOf(
                productDailyBalanceAvgList.stream().map(
                                ProductDailyBalanceAvg::getBalanceAvg)
                        .mapToDouble(BigDecimal::doubleValue)
                        .average().getAsDouble()));

        return  dailyBalanceAvgReportReportDetailCredits;

    }

    public static ProductDailyBalanceAvg getProductDailyBalanceAvgFromTransactionResponseDtoList(List<TransactionResponseDTO> transactionResponseDTOList){
        ProductDailyBalanceAvg productDailyBalanceAvg = new ProductDailyBalanceAvg();
        productDailyBalanceAvg.setId(transactionResponseDTOList.get(0).getProduct().getId());
        if (transactionResponseDTOList.get(0).getProduct().getNumber() != null)
            productDailyBalanceAvg.setNumber(
                    transactionResponseDTOList.get(0).getProduct().getNumber()
            );
        if (transactionResponseDTOList.get(0).getProduct().getType() != null)
            productDailyBalanceAvg.setType(
                    ProductDailyBalanceAvg.TypeEnum.fromValue(
                            transactionResponseDTOList.get(0).getProduct().getType()
                    ));
        productDailyBalanceAvg.setBalanceAvg(
                BigDecimal.valueOf( transactionResponseDTOList.stream()
                .map(TransactionResponseDTO::getProduct)
                .mapToDouble(TransactionProductDTO::getBalance)
                .average()
                .getAsDouble()
                ));
        return productDailyBalanceAvg;
    }
}

