package com.nttdata.report_service.service.report;

import com.nttdata.report_service.dto.account.AccountCardDTO;
import com.nttdata.report_service.dto.account.AccountObjectPresentDTO;
import com.nttdata.report_service.dto.account.AccountResponseDTO;
import com.nttdata.report_service.dto.credit.CreditCardDTO;
import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.dto.customer.CustomerAddressDTO;
import com.nttdata.report_service.dto.customer.CustomerResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionPersonDTO;
import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.Product;
import com.nttdata.report_service.service.account.AccountService;
import com.nttdata.report_service.service.credit.CreditService;
import com.nttdata.report_service.service.customer.CustomerService;
import com.nttdata.report_service.service.transaction.TransactionService;
import com.nttdata.report_service.util.exceptions.AccountNotFoundException;
import com.nttdata.report_service.util.exceptions.CreditNotFoundException;
import com.nttdata.report_service.util.exceptions.ReportNotFoundException;
import com.nttdata.report_service.util.exceptions.TransactionNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("Test Report Service")
@ExtendWith(MockitoExtension.class)
class ReportServiceImpTest {


    @Mock
    TransactionService transactionService;

    @Mock
    AccountService accountService;

    @Mock
    CreditService creditService;

    @Mock
    CustomerService customerService;

    @InjectMocks
    ReportServiceImp reportService;

    AccountResponseDTO accountResponseDTO;
    CreditResponseDTO creditResponseDTO;
    CustomerResponseDTO customerResponseDTO;
    TransactionResponseDTO transactionResponseDTO;

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
                .type("PERSONAL")
                .build();
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
                                .type("personal")
                                .build()
                )
                .createdDate(OffsetDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Get Commission Report By Product Id Tests")
    class getCommissionReportByProductId {
        @Test
        @DisplayName("Get Commission Report By Product Id - Valid Date Format")
        void commission_report_valid_date_format() {
            when(transactionService
                    .fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(
                            anyString(), anyString(), any(), any()
                    )).thenReturn(Flux.just(transactionResponseDTO));
            String id = "anyid";
            String from = "2025-08-01T20:39:47";
            String to = "2025-09-30T22:16:08";

            //Correct Dates
            StepVerifier.create(reportService.getCommissionReportByProductId(
                            id, from, to
                    ))
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
            verify(transactionService, times(1)).fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(
                    anyString(), anyString(), any(), any()
            );
        }

        @Test
        @DisplayName("Get Commission Report By Product Id - Invalid Date Format")
        void commission_report_invalid_date_format() {
            String id = "anyid";
            String from = "2025-08-01";
            String to = "2025-09-30";

            Assertions.assertThrows(
                    DateTimeParseException.class,
                    () -> reportService.getCommissionReportByProductId(
                            id, from, to
                    )
            );
        }

        @Test
        @DisplayName("Get Commission Report By Product Id - Valid Date Format")
        void commission_report_empty_response() {
            when(transactionService
                    .fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(
                            anyString(), anyString(), any(), any()
                    )).thenReturn(Flux.empty());
            String id = "anyid";
            String from = "2025-08-01T20:39:47";
            String to = "2025-09-30T22:16:08";

            //Correct Dates
            StepVerifier.create(reportService.getCommissionReportByProductId(
                            id, from, to
                    ))
                    .expectSubscription()
                    .expectErrorMatches(
                            throwable -> throwable instanceof TransactionNotFoundException &&
                                    throwable.getMessage().equals("No transactions found for the given criteria.")
                    )
                    .verify();
            verify(transactionService, times(1)).fetchGetTransactionsByTypeAndProductIdBetweenTimeDate(
                    anyString(), anyString(), any(), any()
            );
        }

    }

    @Nested
    @DisplayName("Get Commission Report By Product Id Tests")
    class getResumeDailyBalanceAvgReportByClientDocument {
        @Test
        @DisplayName("Get Resume Daily Balance Avg Report By Client Document - Transactions Found")
        void daily_balance_avg_report_transactions_found() throws TransactionNotFoundException {
            when(transactionService
                    .fetchGetTransactionsByClientDocumentBetweenTimeDate(
                            anyString(), any(), any()
                    )).thenReturn(Flux.just(transactionResponseDTO));
            String document = "anydocument";

            StepVerifier.create(reportService.getResumeDailyBalanceAvgReportByClientDocument(
                            document
                    ))
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
            verify(transactionService, times(1)).fetchGetTransactionsByClientDocumentBetweenTimeDate(
                    anyString(), any(), any()
            );
        }

        @Test
        @DisplayName("Get Resume Daily Balance Avg Report By Client Document - No Transactions Found")
        void daily_balance_avg_report_no_transactions_found() {
            when(transactionService
                    .fetchGetTransactionsByClientDocumentBetweenTimeDate(
                            anyString(), any(), any()
                    )).thenReturn(Flux.empty());
            String document = "anydocument";

            StepVerifier.create(reportService.getResumeDailyBalanceAvgReportByClientDocument(
                            document
                    ))
                    .expectSubscription()
                    .expectErrorMatches(
                            throwable -> throwable instanceof TransactionNotFoundException &&
                                    throwable.getMessage().equals("No transactions found for the given criteria.")
                    )
                    .verify();
            verify(transactionService, times(1)).fetchGetTransactionsByClientDocumentBetweenTimeDate(
                    anyString(), any(), any()
            );
        }
    }

    @Nested
    @DisplayName("Get Resume Product General Report Tests")
    class getResumeProductGeneralReport {
        @Test
        @DisplayName("Get Resume Product General Report - Account Found")
        void product_general_report_account_found() {
            when(accountService.fetchGetAccountById(anyString()))
                    .thenReturn(Mono.just(accountResponseDTO));
            when(transactionService.fetchGetTransactionsByProductId(anyString()))
                    .thenReturn(Flux.just(transactionResponseDTO));

            StepVerifier.create(reportService.getResumeProductGeneralReport("anyid"))
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
            verify(accountService, times(1)).fetchGetAccountById(anyString());
            verify(transactionService, times(1)).fetchGetTransactionsByProductId(anyString());
            verify(creditService, times(0)).fetchGetCreditById(anyString());
        }

        @Test
        @DisplayName("Get Resume Product General Report - Credit Found")
        void product_general_report_credit_found() {
            when(accountService.fetchGetAccountById(anyString()))
                    .thenReturn(Mono.error(new Error("Not account for the id anyid found.")));
            when(creditService.fetchGetCreditById(anyString()))
                    .thenReturn(Mono.just(creditResponseDTO));
            when(transactionService.fetchGetTransactionsByProductId(anyString()))
                    .thenReturn(Flux.empty());

            StepVerifier.create(reportService.getResumeProductGeneralReport("anyid"))
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
            verify(accountService, times(1)).fetchGetAccountById(anyString());
            verify(transactionService, times(1)).fetchGetTransactionsByProductId(anyString());
            verify(creditService, times(1)).fetchGetCreditById(anyString());
        }

        @Test
        @DisplayName("Get Resume Product General Report - No Product Found")
        void product_general_report_no_product_found() {
            when(accountService.fetchGetAccountById(anyString()))
                    .thenReturn(Mono.error(new AccountNotFoundException("anyid")));
            when(creditService.fetchGetCreditById(anyString()))
                    .thenReturn(Mono.error(new CreditNotFoundException("anyid")));

            StepVerifier.create(reportService.getResumeProductGeneralReport("anyid"))
                    .expectSubscription()
                    .expectErrorMatches(
                            throwable -> throwable instanceof ReportNotFoundException &&
                                    throwable.getMessage().equals("No report found for the given criteria.")
                    )
                    .verify();
            verify(accountService, times(1)).fetchGetAccountById(anyString());
            verify(transactionService, times(0)).fetchGetTransactionsByProductId(anyString());
            verify(creditService, times(1)).fetchGetCreditById(anyString());
        }
    }

    @Nested
    @DisplayName("Get Resume All Client Products Report Tests")
    class getResumeAllClientProductsReport {
        @Test
        @DisplayName("Get Resume All Client Products Report - Products Found")
        void all_client_products_report_products_found() {
            when(customerService.fetchGetCustomerById(anyString()))
                    .thenReturn(Mono.just(customerResponseDTO));
            when(accountService.fetchGetAccountsByHolderDocument(anyString()))
                    .thenReturn(Flux.just(accountResponseDTO));
            when(creditService.fetchGetCredits())
                    .thenReturn(Flux.just(creditResponseDTO));

            StepVerifier.create(reportService.getResumeAllClientProductsReport("anyid"))
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
            verify(customerService, times(1)).fetchGetCustomerById(anyString());
            verify(accountService, times(1)).fetchGetAccountsByHolderDocument(anyString());
            verify(creditService, times(1)).fetchGetCredits();
        }

        @Test
        @DisplayName("Get Resume All Client Products Report - No Accounts Found")
        void all_client_products_report_no_accounts_found() {
            when(customerService.fetchGetCustomerById(anyString()))
                    .thenReturn(Mono.just(customerResponseDTO));
            when(accountService.fetchGetAccountsByHolderDocument(anyString()))
                    .thenReturn(Flux.empty());
            when(creditService.fetchGetCredits())
                    .thenReturn(Flux.just(creditResponseDTO));

            StepVerifier.create(reportService.getResumeAllClientProductsReport("anyid"))
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
            verify(customerService, times(1)).fetchGetCustomerById(anyString());
            verify(accountService, times(1)).fetchGetAccountsByHolderDocument(anyString());
            verify(creditService, times(1)).fetchGetCredits();
        }

        @Test
        @DisplayName("Get Resume All Client Products Report - No Credits Found")
        void all_client_products_report_no_credits_found() {
            when(customerService.fetchGetCustomerById(anyString()))
                    .thenReturn(Mono.just(customerResponseDTO));
            when(accountService.fetchGetAccountsByHolderDocument(anyString()))
                    .thenReturn(Flux.just(accountResponseDTO));
            when(creditService.fetchGetCredits())
                    .thenReturn(Flux.empty());

            StepVerifier.create(reportService.getResumeAllClientProductsReport("anyid"))
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
            verify(customerService, times(1)).fetchGetCustomerById(anyString());
            verify(accountService, times(1)).fetchGetAccountsByHolderDocument(anyString());
            verify(creditService, times(1)).fetchGetCredits();
        }
    }
}