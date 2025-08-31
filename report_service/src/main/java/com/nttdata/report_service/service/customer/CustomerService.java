package com.nttdata.report_service.service.customer;

import com.nttdata.report_service.dto.account.AccountResponseDTO;
import com.nttdata.report_service.dto.customer.CustomerResponseDTO;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<CustomerResponseDTO> fetchGetCustomerById(String customerId);

}
