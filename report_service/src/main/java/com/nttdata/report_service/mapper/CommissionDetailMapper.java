package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.ComissionDetail;

import java.math.BigDecimal;

public class CommissionDetailMapper {

    public static ComissionDetail getCommissionDetailFromTransactionResponseDto(TransactionResponseDTO transactionResponseDTO){
        ComissionDetail comissionDetail = new ComissionDetail();
        comissionDetail.setAmount(BigDecimal.valueOf(transactionResponseDTO.getAmount()));
        comissionDetail.setCreatedDate(transactionResponseDTO.getCreatedDate());
        comissionDetail.setTransactionNumber(transactionResponseDTO.getNumber());
        return  comissionDetail;
    }
}
