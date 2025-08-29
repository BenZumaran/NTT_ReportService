package com.nttdata.report_service.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CreditClient {

    @Value("${external.credit.service.uri}")
    private String CREDIT_URI;

    @Bean
    public WebClient creditWebClient(){
        return WebClient.builder().baseUrl(CREDIT_URI).build();
    }

}
