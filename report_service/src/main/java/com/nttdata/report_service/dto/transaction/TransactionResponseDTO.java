package com.nttdata.report_service.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionResponseDTO {
    private String id;
    private int number;
    private TransactionProductDTO product;
    private String type;
    private TransactionClientDTO client;
    private double amount;
    private LocalDateTime createdDate;
    private TransactionPersonDTO holder;
    private TransactionPersonDTO signatory;

    @JsonCreator
    public TransactionResponseDTO(
            @JsonProperty("id") String id,
            @JsonProperty("number") int number,
            @JsonProperty("product") TransactionProductDTO product,
            @JsonProperty("type") String type,
            @JsonProperty("client") TransactionClientDTO client,
            @JsonProperty("amount") double amount,
            @JsonProperty("createdDate") LocalDateTime createdDate,
            @JsonProperty("holder") TransactionPersonDTO holder,
            @JsonProperty("signatory") TransactionPersonDTO signatory
    ){
        this.id = id;
        this.number = number;
        this.product = product;
        this.type = type;
        this.client = client;
        this.amount = amount;
        this.createdDate = createdDate;
        this.holder = holder;
        this.signatory = signatory;

    }

}