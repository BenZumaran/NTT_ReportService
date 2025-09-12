package com.nttdata.report_service.service.customer;

import com.nttdata.report_service.dto.customer.CustomerAddressDTO;
import com.nttdata.report_service.dto.customer.CustomerResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.repository.CustomerClient;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Test Customer Service")
@ExtendWith(MockitoExtension.class)
class CustomerServiceImpTest {
    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Spy
    CustomerClient client;

    @InjectMocks
    CustomerServiceImp service;

    CustomerResponseDTO customerResponseDTO;

    @BeforeEach
    void setUp() {
        customerResponseDTO = CustomerResponseDTO.builder()
                .id("customerid")
                .active("ACTIVE")
                .address(
                        CustomerAddressDTO.builder()
                                .city("Lima")
                                .country("Peru")
                                .line1("Any Address")
                                .build()
                )
                .documentNumber("12345678")
                .email("customeremail")
                .firstName("First Name")
                .lastName("Last Name")
                .phone("987654321")
                .type("personal")
                .build();
    }

    @Test
    void fetchGetCustomerById() {
        Mockito.when(client.customerWebClient()).thenReturn(webClient);
        Mockito.when(webClient.get()).thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri("/" + anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToMono(CustomerResponseDTO.class)).thenReturn(Mono.just(customerResponseDTO));

        StepVerifier.create(service.fetchGetCustomerById("anyid"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

        Mockito.when(responseSpec.bodyToMono(CustomerResponseDTO.class)).thenReturn(Mono.empty());

        StepVerifier.create(service.fetchGetCustomerById("anyid"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

        verify(client, times(2)).customerWebClient();
        verify(webClient, times(2)).get();
        verify(requestHeadersUriSpec, times(2)).uri("/" + anyString());
        verify(requestHeadersSpec, times(2)).retrieve();
        verify(responseSpec, times(2)).bodyToMono(CustomerResponseDTO.class);
    }
}