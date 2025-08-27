package com.nttdata.report_service.service.transaction;

import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.repository.TransactionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TransactionServiceImp implements TransactionService{

    @Autowired
    TransactionClient transactionClient;

    @Override
    public Mono<TransactionResponseDTO> fetchGetTransactionsById(String transactionId) {
        return transactionClient.transactionWebClient().get().uri("/"+transactionId)
                .retrieve().bodyToMono(TransactionResponseDTO.class);
    }

    @Override
    public Flux<TransactionResponseDTO> fetchGetTransactionsList() {
        return transactionClient.transactionWebClient().get().uri("")
                .retrieve().bodyToFlux(TransactionResponseDTO.class);
    }

}
