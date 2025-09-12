package com.nttdata.report_service.service.credit;

import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

    Mono<CreditResponseDTO> fetchGetCreditById(String clientId);

    Flux<CreditResponseDTO> fetchGetCredits();

}
