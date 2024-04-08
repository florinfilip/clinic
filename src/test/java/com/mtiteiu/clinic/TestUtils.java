package com.mtiteiu.clinic;

import com.mtiteiu.clinic.dao.UserRegistrationRequest;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;

import java.util.List;
import java.util.Set;

import static com.mtiteiu.clinic.Constants.*;

public class TestUtils {

    public static User createUser(String email, String password, String phoneNumber, PatientDetails patientDetails) {
        return User.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .enabled(true)
                .roles(Set.of(new Role("USER")))
                .patient(patientDetails)
                .build();
    }

    public static User createDefaultUser() {
        return createUser(PASSWORD,
                EMAIL,
                PHONE_NUMBER,
                PatientDetails.builder()
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .phoneNumber(PHONE_NUMBER)
                        .build());
    }

    public static List<User> createUserList() {
        return List.of(createDefaultUser(), createDefaultUser(), createDefaultUser());
    }

    public static UserRegistrationRequest createRegistrationRequest(String username, String lastName, String password, String phoneNumber, String email) {
        return UserRegistrationRequest.builder()
                .firstName(username)
                .lastName(lastName)
                .password(password)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();
    }

    public static UserRegistrationRequest createDefaultRegistrationRequest() {
        return createRegistrationRequest(
                "firstName",
                "lastName",
                "password",
                "0762537817",
                "test@test.com");
    }
}
