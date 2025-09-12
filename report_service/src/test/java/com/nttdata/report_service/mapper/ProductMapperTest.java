package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.account.AccountCardDTO;
import com.nttdata.report_service.dto.account.AccountResponseDTO;
import com.nttdata.report_service.dto.credit.CreditCardDTO;
import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.model.Product;
import com.nttdata.report_service.model.ProductDetail;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductMapperTest {

    @Test
    void getProductFromTransactionProductDto() {
        TransactionProductDTO dto = TransactionProductDTO.builder()
                .id("prod1")
                .type("savings_account")
                .number("12345")
                .balance(500.0)
                .limit(1000.0)
                .build();
        Product product = ProductMapper.getProductFromTransactionProductDto(dto);
        assertNotNull(product);
        assertEquals("prod1", product.getId());
        assertEquals(Product.TypeEnum.SAVINGS_ACCOUNT, product.getType());
        assertEquals("12345", product.getNumber());
    }

    @Test
    void getProductDetailFromExternalResponse() {
        AccountCardDTO card = AccountCardDTO.builder().id("card1").build();
        AccountResponseDTO savings = AccountResponseDTO.builder()
                .id("acc1")
                .accountNumber("1111")
                .accountType("SAVINGS")
                .balance(100.0)
                .linkedCard(card)
                .build();
        ProductDetail detailSavings = ProductMapper.getProductDetailFromExternalResponse(savings);
        assertNotNull(detailSavings);
        assertEquals("acc1", detailSavings.getId());
        assertEquals(ProductDetail.TypeEnum.SAVINGS_ACCOUNT, detailSavings.getType());
        assertEquals(BigDecimal.valueOf(100.0), detailSavings.getBalance());
        assertEquals("card1", detailSavings.getCard());
        assertEquals("1111", detailSavings.getNumber());

        AccountResponseDTO checking = AccountResponseDTO.builder()
                .id("acc2")
                .accountNumber("2222")
                .accountType("CHECKING")
                .balance(200.0)
                .linkedCard(card)
                .build();
        ProductDetail detailChecking = ProductMapper.getProductDetailFromExternalResponse(checking);
        assertEquals(ProductDetail.TypeEnum.CHECKING_ACCOUNT, detailChecking.getType());

        AccountResponseDTO fixedTerm = AccountResponseDTO.builder()
                .id("acc3")
                .accountNumber("3333")
                .accountType("FIXED_TERM")
                .balance(300.0)
                .linkedCard(card)
                .build();
        ProductDetail detailFixedTerm = ProductMapper.getProductDetailFromExternalResponse(fixedTerm);
        assertEquals(ProductDetail.TypeEnum.FIXED_TERM_ACCOUNT, detailFixedTerm.getType());
    }

    @Test
    void testGetProductDetailFromExternalResponse() {
        CreditCardDTO card = CreditCardDTO.builder().id("ccard1").build();
        CreditResponseDTO personal = CreditResponseDTO.builder()
                .id("cred1")
                .type("PERSONAL")
                .balance(1000.0)
                .card(card)
                .build();
        ProductDetail detailPersonal = ProductMapper.getProductDetailFromExternalResponse(personal);
        assertNotNull(detailPersonal);
        assertEquals("cred1", detailPersonal.getId());
        assertEquals(ProductDetail.TypeEnum.PERSONAL_CREDIT, detailPersonal.getType());
        assertEquals(BigDecimal.valueOf(1000.0), detailPersonal.getBalance());
        assertEquals("ccard1", detailPersonal.getCard());

        CreditResponseDTO business = CreditResponseDTO.builder()
                .id("cred2")
                .type("BUSINESS")
                .balance(2000.0)
                .card(card)
                .build();
        ProductDetail detailBusiness = ProductMapper.getProductDetailFromExternalResponse(business);
        assertEquals(ProductDetail.TypeEnum.BUSINESS_CREDIT, detailBusiness.getType());

        CreditResponseDTO creditCard = CreditResponseDTO.builder()
                .id("cred3")
                .type("CREDIT_CARD")
                .balance(3000.0)
                .card(card)
                .build();
        ProductDetail detailCreditCard = ProductMapper.getProductDetailFromExternalResponse(creditCard);
        assertEquals(ProductDetail.TypeEnum.CREDIT_CARD, detailCreditCard.getType());
    }
}