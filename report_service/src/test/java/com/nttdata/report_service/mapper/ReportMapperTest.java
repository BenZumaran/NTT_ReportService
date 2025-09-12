package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.dto.transaction.TransactionResponseDTO;
import com.nttdata.report_service.model.*;
import org.junit.jupiter.api.Test;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportMapperTest {

    private TransactionResponseDTO createTransaction(double amount) {
        TransactionProductDTO product = TransactionProductDTO.builder()
                .id("prod1")
                .type("savings_account")
                .number("12345")
                .balance(amount)
                .limit(1000)
                .build();
        return TransactionResponseDTO.builder()
                .id("tx1")
                .number(1)
                .product(product)
                .type("commission")
                .amount(amount)
                .createdDate(OffsetDateTime.now())
                .build();
    }

    @Test
    void getComissionReportFromTransactionResponseDtoList() {
        List<TransactionResponseDTO> transactions = Arrays.asList(
                createTransaction(100.0),
                createTransaction(200.0)
        );
        CommissionReport report = ReportMapper.getComissionReportFromTransactionResponseDtoList(transactions);
        assertNotNull(report);
        assertNotNull(report.getReportDate());
        assertNotNull(report.getProduct());
        assertEquals(BigDecimal.valueOf(300.0), report.getTotalAmount());
        assertEquals(2, report.getCommissionsDetail().size());
    }

    @Test
    void getGeneralProductReportFromTransactionListAndProductDetail() {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId("prod1");
        Transaction transaction = new Transaction();
        transaction.setId("tx1");
        List<Transaction> transactions = Collections.singletonList(transaction);
        GeneralProductReport report = ReportMapper.getGeneralProductReportFromTransactionListAndProductDetail(
                Tuples.of(productDetail, transactions));
        assertNotNull(report);
        assertNotNull(report.getReportDate());
        assertEquals(productDetail, report.getProduct());
        assertEquals(transactions, report.getTransactions());
    }

    @Test
    void getTotalProductsClientReportFromClient() {
        Client client = new Client();
        client.setId("client1");
        TotalProductsClientReport report = ReportMapper.getTotalProductsClientReportFromClient(client);
        assertNotNull(report);
        assertEquals(client, report.getClient());
        assertNotNull(report.getReportDate());
        assertNotNull(report.getReportDetail());
        assertNotNull(report.getReportDetail().getAccounts());
        assertNotNull(report.getReportDetail().getCredits());
        assertEquals(BigDecimal.ZERO, report.getTotalProducts());
    }

    @Test
    void updateTotalProductsClientReportFromProduct() {
        Client client = new Client();
        client.setId("client1");
        TotalProductsClientReport report = ReportMapper.getTotalProductsClientReportFromClient(client);
        ProductDetail accountDetail = new ProductDetail();
        accountDetail.setId("acc1");
        accountDetail.setType(ProductDetail.TypeEnum.SAVINGS_ACCOUNT);
        ProductDetail creditDetail = new ProductDetail();
        creditDetail.setId("cred1");
        creditDetail.setType(ProductDetail.TypeEnum.PERSONAL_CREDIT);
        // Test accounts
        List<ProductDetail> accountList = Collections.singletonList(accountDetail);
        TotalProductsClientReport updatedReport = ReportMapper.updateTotalProductsClientReportFromProduct(report, accountList);
        assertEquals(BigDecimal.valueOf(1), updatedReport.getTotalProducts());
        assertEquals(accountList, updatedReport.getReportDetail().getAccounts().getProducts());
        assertEquals(BigDecimal.valueOf(1), updatedReport.getReportDetail().getAccounts().getAmount());
        // Test credits
        List<ProductDetail> creditList = Collections.singletonList(creditDetail);
        updatedReport = ReportMapper.updateTotalProductsClientReportFromProduct(report, creditList);
        assertEquals(BigDecimal.valueOf(2), updatedReport.getTotalProducts());
        assertEquals(creditList, updatedReport.getReportDetail().getCredits().getProducts());
        assertEquals(BigDecimal.valueOf(1), updatedReport.getReportDetail().getCredits().getAmount());
    }
}