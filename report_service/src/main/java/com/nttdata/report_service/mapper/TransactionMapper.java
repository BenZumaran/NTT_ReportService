package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.Transaction;

import java.math.BigDecimal;

public class TransactionMapper {

    public static Transaction getTransactionFromTransactionResponseDto(TransactionResponseDTO transactionResponseDTO){
        Transaction transaction = new Transaction();

        transaction.setId(transactionResponseDTO.getId());
        transaction.setAmount(BigDecimal.valueOf(transactionResponseDTO.getAmount()));
        transaction.setNumber(transactionResponseDTO.getNumber());
        transaction.setType(transactionResponseDTO.getType());
        transaction.setSender(
                ProductMapper.getProductFromTransactionProductDto(transactionResponseDTO.getProduct()));
        if (transactionResponseDTO.getReceiver() != null)
            transaction.setReceiver(
                    ProductMapper.getProductFromTransactionProductDto(transactionResponseDTO.getReceiver()));
        transaction.setCreatedDate(transactionResponseDTO.getCreatedDate());
        if (transactionResponseDTO.getClient() != null)
            transaction.setHolderDocument(transactionResponseDTO.getClient().getDocument());
        if (transactionResponseDTO.getSignatory() != null)
            transaction.setSignatoryDocument(transactionResponseDTO.getSignatory().getDocument());

        return  transaction;
    }

}
