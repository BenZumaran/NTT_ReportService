package com.nttdata.report_service.service.customer;

import com.nttdata.report_service.dto.customer.CustomerResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.repository.CustomerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImp implements  CustomerService {

    @Autowired
    CustomerClient customerClient;

    @Override
    public Mono<CustomerResponseDTO> fetchGetCustomerById(String customerId) {
        return customerClient.customerWebClient().get().uri("/"+customerId)
                .retrieve().bodyToMono(CustomerResponseDTO.class);
    }
}
