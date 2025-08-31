package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.CommissionDetail;

import java.math.BigDecimal;

public class CommissionDetailMapper {

    public static CommissionDetail getCommissionDetailFromTransactionResponseDto(TransactionResponseDTO transactionResponseDTO){
        CommissionDetail commissionDetail = new CommissionDetail();
        commissionDetail.setAmount(BigDecimal.valueOf(transactionResponseDTO.getAmount()));
        commissionDetail.setCreatedDate(transactionResponseDTO.getCreatedDate());
        commissionDetail.setTransactionNumber(transactionResponseDTO.getNumber());
        return  commissionDetail;
    }
}
