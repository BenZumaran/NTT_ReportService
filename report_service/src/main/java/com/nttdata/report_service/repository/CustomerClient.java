package com.nttdata.report_service.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CustomerClient {

    @Value("${external.customer.service.uri}")
    private String CUSTOMER_URI;


    @Bean
    public WebClient customerWebClient(){
        return WebClient.builder().baseUrl(CUSTOMER_URI).build();
    }

}
