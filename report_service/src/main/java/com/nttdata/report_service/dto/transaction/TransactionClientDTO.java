package com.nttdata.report_service.dto.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class TransactionClientDTO {
    private String id;
    private String type;
    private String document;

    @JsonCreator
    public TransactionClientDTO(
            @JsonProperty("id") String id,
            @JsonProperty("type") String type,
            @JsonProperty("document") String document
    ){
        this.id = id;
        this.type = type;
        this.document = document;
    }

}