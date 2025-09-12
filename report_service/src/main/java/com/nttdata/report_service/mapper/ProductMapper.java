package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.account.AccountResponseDTO;
import com.nttdata.report_service.dto.credit.CreditResponseDTO;
import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.model.Product;
import com.nttdata.report_service.model.ProductDetail;

import javax.sound.sampled.Port;
import java.math.BigDecimal;

public class ProductMapper {

    public static Product getProductFromTransactionProductDto(TransactionProductDTO transactionProductDTO){
        Product product = new Product();
        product.setId(transactionProductDTO.getId());
        if (transactionProductDTO.getType() != null)
            product.setType(Product.TypeEnum.fromValue(transactionProductDTO.getType()));
        if (transactionProductDTO.getNumber() != null)
            product.setNumber(transactionProductDTO.getNumber());
        return  product;
    }

    public  static ProductDetail getProductDetailFromExternalResponse(AccountResponseDTO accountResponseDTO){
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(accountResponseDTO.getId());
        //SAVINGS, CHECKING, FIXED_TERM
        //savings_account, checking_account, fixed_term_account
        switch (accountResponseDTO.getAccountType()){
            case "SAVINGS":
                productDetail.setType(ProductDetail.TypeEnum.fromValue("savings_account"));
                break;
            case "CHECKING":
                productDetail.setType(ProductDetail.TypeEnum.fromValue("checking_account"));
                break;
            case "FIXED_TERM":
                productDetail.setType(ProductDetail.TypeEnum.fromValue("fixed_term_account"));
                break;
        }

        productDetail.setBalance(BigDecimal.valueOf(accountResponseDTO.getBalance()));
        productDetail.setCard(accountResponseDTO.getLinkedCard().getId());
        productDetail.setNumber(accountResponseDTO.getAccountNumber());

        return  productDetail;
    }

    public  static ProductDetail getProductDetailFromExternalResponse(CreditResponseDTO creditResponseDTO){
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(creditResponseDTO.getId());
        //PERSONAL, BUSINESS, CREDIT_CARD
        //personal_credit, business_credit, credit_card
        switch (creditResponseDTO.getType()){
            case "PERSONAL":
                productDetail.setType(ProductDetail.TypeEnum.fromValue("personal_credit"));
                break;
            case "BUSINESS":
                productDetail.setType(ProductDetail.TypeEnum.fromValue("business_credit"));
                break;
            case "CREDIT_CARD":
                productDetail.setType(ProductDetail.TypeEnum.fromValue("credit_card"));
                break;
        }
        productDetail.setBalance(BigDecimal.valueOf(creditResponseDTO.getBalance()));
        if (creditResponseDTO.getCard() != null)
            productDetail.setCard(creditResponseDTO.getCard().getId());

        return  productDetail;
    }

}
