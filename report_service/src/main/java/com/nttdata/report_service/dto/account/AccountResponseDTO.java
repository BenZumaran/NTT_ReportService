package com.nttdata.report_service.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AccountResponseDTO {
    private String id;
    private String accountNumber;
    private String interbankNumber;
    private String holderDocument;
    private String[] authorizedSigners;
    private String accountType;
    private double balance = 0.0;
    private double interestRate = 9.0;
    private int monthlyMovementLimit = -1;
    private double maintenanceFee = 0;
    private int allowedDayOfMonth;
    private LocalDate creationDate = LocalDate.now();
    private boolean active = true;
    private AccountResponseDTO linkedCard;

    @JsonCreator
    public AccountResponseDTO(
            @JsonProperty("id") String id,
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("interbankNumber") String interbankNumber,
            @JsonProperty("holderDocument") String holderDocument,
            @JsonProperty("authorizedSigners") String[] authorizedSigners,
            @JsonProperty("accountType") String accountType,
            @JsonProperty("balance") double balance,
            @JsonProperty("interestRate") double interestRate,
            @JsonProperty("monthlyMovementLimit") int monthlyMovementLimit,
            @JsonProperty("maintenanceFee") double maintenanceFee,
            @JsonProperty("allowedDayOfMonth") int allowedDayOfMonth,
            @JsonProperty("creationDate") LocalDate creationDate,
            @JsonProperty("active") boolean active,
            @JsonProperty("linkedCar") AccountResponseDTO linkedCard
    ) {
         this.id = id;
         this.accountNumber = accountNumber;
         this.interbankNumber = interbankNumber;
         this.holderDocument = holderDocument;
         this.authorizedSigners = authorizedSigners;
         this.accountType = accountType;
         this.balance = balance;
         this.interestRate = interestRate;
         this.monthlyMovementLimit = monthlyMovementLimit;
         this.maintenanceFee = maintenanceFee;
         this.allowedDayOfMonth = allowedDayOfMonth;
         this.creationDate = creationDate;
         this.active = active;
         this.linkedCard = linkedCard;
    }

    /*
    {
  "id": "string",
  "accountNumber": "string",
  "interbankNumber": "string",
  "holderDocument": "string",
  "authorizedSigners": [
    "string"
  ],
  "accountType": "SAVINGS",
  "balance": 0,
  "interestRate": 0,
  "monthlyMovementLimit": 0,
  "maintenanceFee": 0,
  "allowedDayOfMonth": 0,
  "creationDate": "2025-08-23",
  "active": true,
  "linkedCard": {
    "id": "string"
  }
}
     */
}
