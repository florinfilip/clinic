package com.mtiteiu.clinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class UserDTO {
    String cnp;
    LocalDate dateOfBirth;
    String email;
    String password;
    String repeatPassword;
    String firstName;
    String lastName;
    String phoneNumber;
}
