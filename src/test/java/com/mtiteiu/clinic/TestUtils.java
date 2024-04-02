package com.mtiteiu.clinic;

import com.mtiteiu.clinic.dao.UserRegistrationRequest;
import com.mtiteiu.clinic.model.user.User;

public class TestUtils {

    public static User createUser(String email, String password, String phoneNumber) {
        return User.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .enabled(true)
                .build();
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

    public static User createDefaultUser() {
        return createUser("password",
                "test@test.com",
                "0762537817"
        );
    }
}
