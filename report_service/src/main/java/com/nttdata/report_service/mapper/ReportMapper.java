package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class ReportMapper {

    //Se convierte List<TransactionResponseDTO> a ComissionReport
    public static ComissionReport getComissionReportFromTransactionResponseDtoList(List<TransactionResponseDTO> transactionResponseDTOS){

        ComissionReport comissionReport = new ComissionReport();
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
        comissionReport.setComissionsDetail(transactionResponseDTOS.stream()
                .map(CommissionDetailMapper::getCommissionDetailFromTransactionResponseDto)
                .collect(Collectors.toList()));

        return comissionReport;
    }
}
