package com.nttdata.report_service.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionPersonDTO {
    private String id;
    private String document;
    private String fullName;
    private String type;


    @JsonCreator
    public TransactionPersonDTO(
            @JsonProperty("id") String id,
            @JsonProperty("document") String document,
            @JsonProperty("type") String type,
            @JsonProperty("fullName") String fullName
    ){
        this.id = id;
        this.document = document;
        this.type = type;
        this.fullName = fullName;
    }


}