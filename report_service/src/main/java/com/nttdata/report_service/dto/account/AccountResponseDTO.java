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
    private AccountObjectPresentDTO monthlyMovementLimit;
    private AccountObjectPresentDTO maintenanceFee;
    private AccountObjectPresentDTO allowedDayOfMonth;
    private LocalDate creationDate = LocalDate.now();
    private boolean active = true;
    private AccountCardDTO linkedCard;
    private double freeTransactionsLimit;
    private double commissionFee;

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
            @JsonProperty("monthlyMovementLimit") AccountObjectPresentDTO monthlyMovementLimit,
            @JsonProperty("maintenanceFee") AccountObjectPresentDTO maintenanceFee,
            @JsonProperty("allowedDayOfMonth") AccountObjectPresentDTO allowedDayOfMonth,
            @JsonProperty("creationDate") LocalDate creationDate,
            @JsonProperty("active") boolean active,
            @JsonProperty("linkedCar") AccountCardDTO linkedCard,
            @JsonProperty("freeTransactionsLimit") double freeTransactionsLimit,
            @JsonProperty("commissionFee") double commissionFee
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
         this.freeTransactionsLimit = freeTransactionsLimit;
         this.commissionFee = commissionFee;
    }

}
