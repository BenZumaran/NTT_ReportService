package com.nttdata.report_service.service.credit;

import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.repository.CreditClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImp implements CreditService{

    @Autowired
    CreditClient creditClient;

    @Override
    public Mono<CreditResponseDTO> fetchGetCreditById(String clientId) {
        return creditClient.creditWebClient().get().uri(clientId)
                .retrieve().bodyToMono(CreditResponseDTO.class);
    }

}
