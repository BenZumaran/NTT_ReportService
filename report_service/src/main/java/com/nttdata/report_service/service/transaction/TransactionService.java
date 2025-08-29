package com.nttdata.report_service.service.transaction;

import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Flux<TransactionResponseDTO> fetchGetTransactionsList();

    Flux<TransactionResponseDTO> fetchGetTransactionsByClientDocumentBetweenTimeDate(String document, LocalDateTime from, LocalDateTime to);

    Flux<TransactionResponseDTO> fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(String type, String productId, LocalDateTime from, LocalDateTime to);


}
