package com.nttdata.report_service.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TransactionClient {

    @Value("${external.transaction.service.uri}")
    private String TRANSACTION_URI;

    @Bean
    public WebClient transactionWebClient(){
        return WebClient.builder().baseUrl(TRANSACTION_URI).build();
    }

}
