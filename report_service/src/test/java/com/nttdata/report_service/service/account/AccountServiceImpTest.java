package com.nttdata.report_service.service.account;

import com.nttdata.report_service.dto.account.AccountCardDTO;
import com.nttdata.report_service.dto.account.AccountObjectPresentDTO;
import com.nttdata.report_service.dto.account.AccountResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.repository.AccountClient;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test Account Service")
@ExtendWith(MockitoExtension.class)
class AccountServiceImpTest {

    @Mock
    WebClient webClient;

    @Mock
    UriBuilder uriBuilder;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Spy
    AccountClient client;

    @InjectMocks
    AccountServiceImp service;

    AccountResponseDTO accountResponseDTO;
    

    @BeforeEach
    void setUp() {
        accountResponseDTO = AccountResponseDTO.builder()
                .id("accountid")
                .accountType("savings_account")
                .accountNumber("12345678910")
                .active(true)
                .allowedDayOfMonth(
                        AccountObjectPresentDTO.builder().present(true).build()
                )
                .authorizedSigners(new String[]{""})
                .balance(100.0)
                .commissionFee(10)
                .creationDate(LocalDate.now())
                .freeTransactionsLimit(15)
                .holderDocument("12345678")
                .interbankNumber("12345123456789101234")
                .interestRate(5)
                .linkedCard(
                        AccountCardDTO.builder()
                                .id("cardid")
                                .build()
                )
                .maintenanceFee(
                        AccountObjectPresentDTO.builder().present(true).build()
                )
                .monthlyMovementLimit(
                        AccountObjectPresentDTO.builder().present(true).build()
                )
                .build();
    }

    @Test
    void fetchGetAccountById() {
        Mockito.when(client.accountWebClient()).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri("/" + anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(AccountResponseDTO.class)).thenReturn(Mono.just(accountResponseDTO));

        StepVerifier.create(service.fetchGetAccountById("anyid"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        Mockito.when(responseSpec.bodyToMono(AccountResponseDTO.class)).thenReturn(Mono.empty());

        StepVerifier.create(service.fetchGetAccountById("anyid"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(client, times(2)).accountWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri("/" + anyString());
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToMono(AccountResponseDTO.class);
    }

    @Test
    void fetchGetAccountsByHolderDocument() {
        Mockito.when(client.accountWebClient()).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri("/holder/" + anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToFlux(AccountResponseDTO.class)).thenReturn(Flux.just(accountResponseDTO));

        StepVerifier.create(service.fetchGetAccountsByHolderDocument("anydocument"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        Mockito.when(responseSpec.bodyToFlux(AccountResponseDTO.class)).thenReturn(Flux.empty());

        StepVerifier.create(service.fetchGetAccountsByHolderDocument("anydocument"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(client, times(2)).accountWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri("/holder/" + anyString());
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToFlux(AccountResponseDTO.class);
    }
}