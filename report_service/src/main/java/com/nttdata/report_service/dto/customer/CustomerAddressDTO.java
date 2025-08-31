package com.nttdata.report_service.dto.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerAddressDTO {

    private String line1;
    private String city;
    private String country;

    @JsonCreator
    public CustomerAddressDTO(
            @JsonProperty("line1") String line1,
            @JsonProperty("city") String city,
            @JsonProperty("country") String country){
        this.line1 = line1;
        this.city = city;
        this.country = country;
    }


}
