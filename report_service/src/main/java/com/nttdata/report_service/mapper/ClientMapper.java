package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionPersonDTO;
import com.nttdata.report_service.model.Client;

public class ClientMapper {

    public static Client getClientFromTransactionClientDto(TransactionPersonDTO transactionPersonDTO){
        Client client = new Client();

        client.setId(transactionPersonDTO.getId());
        client.setDocument(transactionPersonDTO.getDocument());
        client.setType(Client.TypeEnum.fromValue(transactionPersonDTO.getType()));

        return client;
    }
}
