package com.nttdata.report_service.service.transaction;

import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.repository.TransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionServiceImp implements TransactionService{

    @Autowired
    TransactionClient transactionClient;

    @Override
    public Flux<TransactionResponseDTO> fetchGetTransactionsList() {
        return transactionClient.transactionWebClient().get().uri("")
                .retrieve().bodyToFlux(TransactionResponseDTO.class);
    }

    @Override
    public Flux<TransactionResponseDTO> fetchGetTransactionsByClientDocumentBetweenTimeDate(String document, LocalDateTime from, LocalDateTime to) {
        return transactionClient.transactionWebClient().get().uri(uri ->
                        uri.path("/client/"+document)
                                .queryParam("from",from.format(DateTimeFormatter.ISO_DATE_TIME))
                                .queryParam("to",to.format(DateTimeFormatter.ISO_DATE_TIME))
                                .build()
                ).retrieve().bodyToFlux(TransactionResponseDTO.class);
    }

    @Override
    public Flux<TransactionResponseDTO> fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(String type, String productId, LocalDateTime from, LocalDateTime to) {
        return transactionClient.transactionWebClient().get().uri(uri ->
                uri.path("/"+type+"/product/"+productId)
                        .queryParam("from",from.format(DateTimeFormatter.ISO_DATE_TIME))
                        .queryParam("to",to.format(DateTimeFormatter.ISO_DATE_TIME))
                        .build()
        ).retrieve().bodyToFlux(TransactionResponseDTO.class);
    }
}
