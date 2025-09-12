package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionPersonDTO;
import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.Client;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DailyBalanceObjectMapperTest {

    private TransactionResponseDTO createTransaction(String productId, String productType, double balance) {
        TransactionProductDTO product = TransactionProductDTO.builder()
                .id(productId)
                .type(productType)
                .number("12345")
                .balance(balance)
                .limit(1000)
                .build();
        TransactionPersonDTO client = TransactionPersonDTO.builder()
                .id("client1")
                .document("doc1")
                .type(Client.TypeEnum.PERSONAL.getValue())
                .fullName("John Doe")
                .build();
        return TransactionResponseDTO.builder()
                .id("tx1")
                .number(1)
                .product(product)
                .type("deposit")
                .client(client)
                .amount(balance)
                .createdDate(OffsetDateTime.now())
                .build();
    }

    @Test
    void getDailyBalanceAvgReportFromTransactionResponseDtoList() {
        List<TransactionResponseDTO> transactions = Arrays.asList(
                createTransaction("prod1", "savings_account", 100.0),
                createTransaction("prod2", "personal_credit", 200.0)
        );
        var report = DailyBalanceObjectMapper.getDailyBalanceAvgReportFromTransactionResponseDtoList(transactions);
        assertNotNull(report);
        assertNotNull(report.getReportDetail().getAccounts());
        assertNotNull(report.getReportDetail().getCredits());
        assertNotNull(report.getReportDetail());
        assertEquals(BigDecimal.valueOf(2.0), report.getTotalProducts());
        transactions = Collections.singletonList(
                createTransaction("prod2", "personal_credit", 200.0)
        );
        report = DailyBalanceObjectMapper.getDailyBalanceAvgReportFromTransactionResponseDtoList(transactions);
        assertNull(report.getReportDetail().getAccounts());
    }

    @Test
    void getDailyBalanceAvgReportReportDetailFromTransactionResponseDtoList() {
        List<TransactionResponseDTO> transactions = Arrays.asList(
                createTransaction("prod1", "savings_account", 100.0),
                createTransaction("prod2", "personal_credit", 200.0)
        );
        var detail = DailyBalanceObjectMapper.getDailyBalanceAvgReportReportDetailFromTransactionResponseDtoList(transactions);
        assertNotNull(detail);
        assertNotNull(detail.getAccounts());
        assertNotNull(detail.getCredits());
        assertEquals(BigDecimal.valueOf(1), detail.getAccounts().getTotalAccounts());
        assertEquals(BigDecimal.valueOf(1), detail.getCredits().getTotalCredits());
    }

    @Test
    void getDailyBalanceAvgReportReportDetailAccountsFromTransactionResponseDtoList() {
        List<TransactionResponseDTO> transactions = Arrays.asList(
                createTransaction("prod1", "savings_account", 100.0),
                createTransaction("prod1", "savings_account", 200.0)
        );
        var accounts = DailyBalanceObjectMapper.getDailyBalanceAvgReportReportDetailAccountsFromTransactionResponseDtoList(transactions);
        assertNotNull(accounts);
        assertEquals(BigDecimal.valueOf(1), accounts.getTotalAccounts());
        assertEquals(150.0, accounts.getAccountsAverage().doubleValue());
    }

    @Test
    void getDailyBalanceAvgReportReportDetailCreditsFromTransactionResponseDtoList() {
        List<TransactionResponseDTO> transactions = Arrays.asList(
                createTransaction("prod2", "personal_credit", 300.0),
                createTransaction("prod2", "personal_credit", 500.0)
        );
        var credits = DailyBalanceObjectMapper.getDailyBalanceAvgReportReportDetailCreditsFromTransactionResponseDtoList(transactions);
        assertNotNull(credits);
        assertEquals(BigDecimal.valueOf(1), credits.getTotalCredits());
        assertEquals(400.0, credits.getCreditsAverage().doubleValue());
    }

    @Test
    void getProductDailyBalanceAvgFromTransactionResponseDtoList() {
        List<TransactionResponseDTO> transactions = Arrays.asList(
                createTransaction("prod1", "savings_account", 100.0),
                createTransaction("prod1", "savings_account", 200.0)
        );
        var productAvg = DailyBalanceObjectMapper.getProductDailyBalanceAvgFromTransactionResponseDtoList(transactions);
        assertNotNull(productAvg);
        assertEquals("prod1", productAvg.getId());
        assertEquals(150.0, productAvg.getBalanceAvg().doubleValue());
    }
}