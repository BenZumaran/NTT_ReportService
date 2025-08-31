package com.nttdata.report_service.dto.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String documentNumber;
    private String type;
    private String segment;
    private String phone;
    private String id;
    private String active;
    private CustomerAddressDTO address;

    @JsonCreator
    public CustomerResponseDTO(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("documentNumber") String documentNumber,
            @JsonProperty("type") String type,
            @JsonProperty("segment") String segment,
            @JsonProperty("phone") String phone,
            @JsonProperty("id") String id,
            @JsonProperty("active") String active,
            @JsonProperty("address") CustomerAddressDTO address
    ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.documentNumber = documentNumber;
        this.type = type;
        this.segment = segment;
        this.phone = phone;
        this.id = id;
        this.active = active;
        this.address = address;
    }


}

/*
{
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan.perez@example.pe",
    "documentNumber": "45878932",
    "type": "PERSONAL",
    "segment": "STANDARD",
    "phone": "+51987654321",
    "id": "68b228b4a9937f19ce6b197c"
    "active": true,
    "address": {
        "line1": "Av. Arequipa 1234",
        "city": "Lima",
        "country": "PE"
    },
}
 */
