package com.mtiteiu.clinic.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class UserRegistrationRequest {

    String firstName;
    String lastName;
    String password;
    String email;
    String phoneNumber;

}
