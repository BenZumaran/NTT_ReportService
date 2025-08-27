package com.nttdata.report_service.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountCardDTO {
    private String id;

    @JsonCreator
    public AccountCardDTO(
            @JsonProperty("id") String id){
        this.id = id;
    }

}
