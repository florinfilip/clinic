package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    String county;
    String city;
    String street;
    String number;
    String floor;
    String apartment;
    String postalCode;
}
