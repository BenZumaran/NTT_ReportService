package com.nttdata.report_service.dto.credit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreditResponseDTO {
    private String id;
    private String customerId;
    private String type;
    private double limit;
    private double balance;
    private double interestAnnual;
    private LocalDate dueDate;
    private String status;
    private CreditCardDTO card;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonCreator
    public CreditResponseDTO(
            @JsonProperty("id") String id,
            @JsonProperty("customerId") String customerId,
            @JsonProperty("type") String type,
            @JsonProperty("limit") double limit,
            @JsonProperty("balance") double balance,
            @JsonProperty("interestAnnual") double interestAnnual,
            @JsonProperty("dueDate") LocalDate dueDate,
            @JsonProperty("status") String status,
            @JsonProperty("card") CreditCardDTO card,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("updatedAt") LocalDateTime updatedAt){
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.limit = limit;
        this.balance = balance;
        this.interestAnnual = interestAnnual;
        this.dueDate = dueDate;
        this.status = status;
        this.card = card;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
