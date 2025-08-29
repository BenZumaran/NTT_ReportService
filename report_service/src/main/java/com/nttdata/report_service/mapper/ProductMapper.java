package com.nttdata.report_service.mapper;

import com.nttdata.report_service.dto.transaction.TransactionProductDTO;
import com.nttdata.report_service.model.Product;

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

}
