package com.nttdata.report_service.dto.credit;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditCardDTO {
    private String id;
    private String last4;
    private String brand;

    @JsonCreator
    public CreditCardDTO(
            @JsonProperty("id") String id,
            @JsonProperty("last4") String last4,
            @JsonProperty("brand") String brand){
        this.id = id;
        this.last4 = last4;
        this.brand = brand;
    }

}
