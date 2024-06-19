package com.mtiteiu.clinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mtiteiu.clinic.model.patient.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class UserDTO {
    String cnp;
    LocalDate dateOfBirth;
    Gender gender;
    String email;
    String newPassword;
    String confirmPassword;
    String currentPassword;
    String firstName;
    String lastName;
    String phoneNumber;
}
