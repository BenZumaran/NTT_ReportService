package com.nttdata.report_service.service.transaction;

import com.nttdata.report_service.dto.transaction.TransactionPersonDTO;
import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.Client;
import com.nttdata.report_service.model.Product;
import com.nttdata.report_service.repository.TransactionClient;
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

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@DisplayName("Test Transaction Service")
@ExtendWith(MockitoExtension.class)
class TransactionServiceImpTest {

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
    TransactionClient transactionClient;

    @InjectMocks
    TransactionServiceImp transactionService;

    TransactionResponseDTO transactionResponseDTO;

    @BeforeEach
        void setUp() {
        transactionResponseDTO = TransactionResponseDTO.builder()
                .id("transactionid")
                .number(1)
                .amount(10)
                .product(
                        TransactionProductDTO.builder()
                                .id("productid")
                                .balance(10)
                                .type(Product.TypeEnum.SAVINGS_ACCOUNT.getValue())
                                .build()
                )
                .client(
                        TransactionPersonDTO.builder()
                                .id("personid")
                                .document("12345678")
                                .fullName("Person Full Name")
                                .type(Client.TypeEnum.PERSONAL.getValue())
                                .build()
                )
                .createdDate(OffsetDateTime.now())
                .build();


    }

    @Test
    void fetchGetTransactionsList() {
        Mockito.when(transactionClient.transactionWebClient()).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.just(transactionResponseDTO));

        StepVerifier.create(transactionService.fetchGetTransactionsList())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        Mockito.when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.empty());

        StepVerifier.create(transactionService.fetchGetTransactionsList())
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(transactionClient, times(2)).transactionWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToFlux(TransactionResponseDTO.class);


    }

    @Test
    void fetchGetTransactionsByProductId() {
        Mockito.when(transactionClient.transactionWebClient()).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri("/product/" + anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.just(transactionResponseDTO));

        StepVerifier.create(transactionService.fetchGetTransactionsByProductId("anyid"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        Mockito.when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.empty());

        StepVerifier.create(transactionService.fetchGetTransactionsByProductId("anyid"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(transactionClient, times(2)).transactionWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri("/product/" + anyString());
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToFlux(TransactionResponseDTO.class);    }

    @Test
    void fetchGetTransactionsByClientDocumentBetweenTimeDate() {
        when(transactionClient.transactionWebClient()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri((Function<UriBuilder, URI>) any());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.just(transactionResponseDTO));

        StepVerifier.create(transactionService.fetchGetTransactionsByClientDocumentBetweenTimeDate("anyid", LocalDateTime.now(),LocalDateTime.now()))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.empty());

        StepVerifier.create(transactionService.fetchGetTransactionsByClientDocumentBetweenTimeDate("anyid", LocalDateTime.now(),LocalDateTime.now()))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(transactionClient, times(2)).transactionWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri((Function<UriBuilder, URI>) any());
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToFlux(TransactionResponseDTO.class);
    }

    @Test
    void fetchGetTransactionsByTypeAndProductIdBetweenTimeDate() {
        when(transactionClient.transactionWebClient()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri((Function<UriBuilder, URI>) any());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.just(transactionResponseDTO));

        StepVerifier.create(transactionService.fetchGetTransactionsByTypeAndProductIdBetweenTimeDate("anytype","anyid", LocalDateTime.now(),LocalDateTime.now()))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        when(responseSpec.bodyToFlux(TransactionResponseDTO.class)).thenReturn(Flux.empty());

        StepVerifier.create(transactionService.fetchGetTransactionsByTypeAndProductIdBetweenTimeDate("anytype","anyid", LocalDateTime.now(),LocalDateTime.now()))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(transactionClient, times(2)).transactionWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri((Function<UriBuilder, URI>) any());
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToFlux(TransactionResponseDTO.class);
    }
}