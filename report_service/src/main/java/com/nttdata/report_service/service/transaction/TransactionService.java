package com.nttdata.report_service.service.transaction;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionService {

    Mono<TransactionResponseDTO> fetchGetTransactionsById(String clientId);

    Flux<TransactionResponseDTO> fetchGetTransactionsList();

}
