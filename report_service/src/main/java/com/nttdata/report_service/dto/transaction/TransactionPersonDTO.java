package com.nttdata.report_service.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionPersonDTO {
    private String document;
    private String fullName;
    private String signature;

    @JsonCreator
    public TransactionPersonDTO(
            @JsonProperty("document") String document,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("signature") String signature
    ){
        this.document = document;
        this.fullName = fullName;
        this.signature = signature;
    }


}