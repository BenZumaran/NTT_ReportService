package com.nttdata.report_service.service.account;


import com.nttdata.report_service.dto.account.AccountResponseDTO;
import com.nttdata.report_service.repository.AccountClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    AccountClient accountClient;


    @Override
    public Mono<AccountResponseDTO> fetchGetAccountById(String accountId) {
        return accountClient.accountWebClient().get()
                .uri("/"+accountId).retrieve().bodyToMono(AccountResponseDTO.class);
    }

    @Override
    public Flux<AccountResponseDTO> fetchGetAccountsByHolderDocument(String holderDocument) {
        return accountClient.accountWebClient().get()
                .uri("/holder/"+holderDocument).retrieve().bodyToFlux(AccountResponseDTO.class);
    }

}
