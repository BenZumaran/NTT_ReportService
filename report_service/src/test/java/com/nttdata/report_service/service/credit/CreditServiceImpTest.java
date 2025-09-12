package com.nttdata.report_service.service.credit;

import com.nttdata.report_service.dto.credit.CreditCardDTO;
import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.repository.CreditClient;
import com.nttdata.report_service.repository.TransactionClient;
import com.nttdata.report_service.service.transaction.TransactionServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test Credit Service")
@ExtendWith(MockitoExtension.class)
class CreditServiceImpTest {

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Spy
    CreditClient client;

    @InjectMocks
    CreditServiceImp service;

    CreditResponseDTO creditResponseDTO;

    @BeforeEach
    void setUp() {
        creditResponseDTO = CreditResponseDTO.builder()
                .id("creditid")
                .balance(50)
                .card(
                        CreditCardDTO.builder()
                                .id("cardid")
                                .brand("VISA")
                                .last4("6789")
                                .build()
                )
                .createdAt(LocalDateTime.now())
                .customerId("customerid")
                .dueDate(LocalDate.now())
                .interestAnnual(5.0)
                .limit(200.0)
                .status("ACTIVE")
                .type("credit_card")
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void fetchGetCreditById() {
        Mockito.when(client.creditWebClient()).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri("/" + anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(CreditResponseDTO.class)).thenReturn(Mono.just(creditResponseDTO));

        StepVerifier.create(service.fetchGetCreditById("anyid"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        Mockito.when(responseSpec.bodyToMono(CreditResponseDTO.class)).thenReturn(Mono.empty());

        StepVerifier.create(service.fetchGetCreditById("anyid"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(client, times(2)).creditWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri("/" + anyString());
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToMono(CreditResponseDTO.class);
    }

    @Test
    void fetchGetCredits() {
        Mockito.when(client.creditWebClient()).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToFlux(CreditResponseDTO.class)).thenReturn(Flux.just(creditResponseDTO));

        StepVerifier.create(service.fetchGetCredits())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        Mockito.when(responseSpec.bodyToFlux(CreditResponseDTO.class)).thenReturn(Flux.empty());

        StepVerifier.create(service.fetchGetCredits())
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(client, times(2)).creditWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToFlux(CreditResponseDTO.class);
    }
}