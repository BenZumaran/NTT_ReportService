package com.nttdata.report_service.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AccountClient {

    @Value("${external.account.service.uri}")
    private String ACCOUNT_URI;

    @Bean
    public WebClient accountWebClient(){
        return WebClient.builder().baseUrl(ACCOUNT_URI).build();
    }

}
