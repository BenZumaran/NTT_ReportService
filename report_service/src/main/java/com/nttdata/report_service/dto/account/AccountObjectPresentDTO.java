package com.nttdata.report_service.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class AccountObjectPresentDTO {
    private boolean present;

    @JsonCreator
    public AccountObjectPresentDTO(
            @JsonProperty("present") boolean present
    ){
        this.present = present;
    }

}
