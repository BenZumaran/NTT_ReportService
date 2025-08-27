package com.nttdata.report_service.service.account;

import com.nttdata.report_service.dto.account.AccountResponseDTO;
import reactor.core.publisher.Mono;


public interface AccountService {

    Mono<AccountResponseDTO> fetchGetAccountById(String accountId);

}
