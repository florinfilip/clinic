package com.mtiteiu.clinic.model.patient;

import lombok.Data;

@Data
public class Address {

    String county;
    String city;
    String street;
    String building;
    String number;
    String postalCode;
}
