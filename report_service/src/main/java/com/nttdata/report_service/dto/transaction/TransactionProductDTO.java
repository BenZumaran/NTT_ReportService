package com.nttdata.report_service.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class TransactionProductDTO {
    private String id;
    private String type;
    private String number;
    private double balance;
    private double limit;

    @JsonCreator
    public TransactionProductDTO(
            @JsonProperty("id") String id,
            @JsonProperty("type") String type,
            @JsonProperty("number") String number,
            @JsonProperty("balance") double balance,
            @JsonProperty("limit") double limit
    ){
        this.id = id;
        this.type = type;
        this.number = number;
        this.balance = balance;
        this.limit = limit;
    }



}
