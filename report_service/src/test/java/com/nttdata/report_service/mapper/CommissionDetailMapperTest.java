package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommissionDetailMapperTest {

    @Test
    void getCommissionDetailFromTransactionResponseDto() {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setAmount(150.75);
        dto.setCreatedDate(OffsetDateTime.now());
        dto.setNumber(0);

        var response = CommissionDetailMapper.getCommissionDetailFromTransactionResponseDto(dto);

        assertEquals(BigDecimal.valueOf(150.75), response.getAmount());
        assertNotNull(response.getCreatedDate());
        assertEquals(0, response.getTransactionNumber());
    }
}