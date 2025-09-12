package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.customer.CustomerResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionPersonDTO;
import com.nttdata.report_service.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientMapperTest {

    @Test
    void getClientFromTransactionClientDto() {
        TransactionPersonDTO dto = new TransactionPersonDTO();
        dto.setId("123");
        dto.setDocument("ABC123");
        dto.setType("personal");

        var response = ClientMapper.getClientFromTransactionClientDto(dto);

        assertEquals("123", response.getId());
        assertEquals("ABC123", response.getDocument());
        assertEquals(Client.TypeEnum.PERSONAL, response.getType());

        dto.setType("WRONGTYPE");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ClientMapper.getClientFromTransactionClientDto(dto)
        );

    }

    @Test
    void getClientFromCustomerResponseDto() {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId("456");
        dto.setType("PERSONAL");
        dto.setDocumentNumber("XYZ789");

        Client client = ClientMapper.getClientFromCustomerResponseDto(dto);

        assertEquals("456", client.getId());
        assertEquals("XYZ789", client.getDocument());
        assertEquals(Client.TypeEnum.PERSONAL, client.getType());

        dto.setType("BUSINESS");
        client = ClientMapper.getClientFromCustomerResponseDto(dto);
        assertEquals(Client.TypeEnum.BUSINESS, client.getType());

        dto.setType("WRONGTYPE");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ClientMapper.getClientFromCustomerResponseDto(dto)

        );
    }

}