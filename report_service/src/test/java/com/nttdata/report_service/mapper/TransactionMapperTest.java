package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionPersonDTO;
import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionMapperTest {

    private TransactionResponseDTO createTransactionResponseDTO() {
        TransactionProductDTO sender = TransactionProductDTO.builder()
                .id("prod1")
                .type("savings_account")
                .number("12345")
                .balance(500.0)
                .limit(1000.0)
                .build();
        TransactionProductDTO receiver = TransactionProductDTO.builder()
                .id("prod2")
                .type("checking_account")
                .number("54321")
                .balance(300.0)
                .limit(500.0)
                .build();
        TransactionPersonDTO client = TransactionPersonDTO.builder()
                .id("client1")
                .document("DOC123")
                .type("type1")
                .fullName("John Doe")
                .build();
        TransactionPersonDTO signatory = TransactionPersonDTO.builder()
                .id("sign1")
                .document("DOC999")
                .type("type2")
                .fullName("Jane Smith")
                .build();
        return TransactionResponseDTO.builder()
                .id("tx1")
                .number(42)
                .product(sender)
                .receiver(receiver)
                .type("transfer")
                .client(client)
                .signatory(signatory)
                .amount(123.45)
                .createdDate(OffsetDateTime.now())
                .build();
    }

    @Test
    void getTransactionFromTransactionResponseDto() {
        TransactionResponseDTO dto = createTransactionResponseDTO();
        Transaction transaction = TransactionMapper.getTransactionFromTransactionResponseDto(dto);
        assertNotNull(transaction);
        assertEquals("tx1", transaction.getId());
        assertEquals(BigDecimal.valueOf(123.45), transaction.getAmount());
        assertEquals(42, transaction.getNumber());
        assertEquals("transfer", transaction.getType());
        assertNotNull(transaction.getSender());
        assertEquals("prod1", transaction.getSender().getId());
        assertNotNull(transaction.getReceiver());
        assertEquals("prod2", transaction.getReceiver().getId());
        assertEquals(dto.getCreatedDate(), transaction.getCreatedDate());
        assertEquals("DOC123", transaction.getHolderDocument());
        assertEquals("DOC999", transaction.getSignatoryDocument());
    }


}