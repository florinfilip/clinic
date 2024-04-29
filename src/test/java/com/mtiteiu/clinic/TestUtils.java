package com.mtiteiu.clinic;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.mtiteiu.clinic.Constants.*;

@Slf4j
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

    public static UserDTO createRegistrationRequest(String username, String lastName, String password, String rPassword, String phoneNumber, String email) {
        return UserDTO.builder()
                .firstName(username)
                .lastName(lastName)
                .password(password)
                .repeatPassword(rPassword)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();
    }

    public static UserDTO createDefaultRegistrationRequest() {
        return createRegistrationRequest(
                "firstName",
                "lastName",
                "password",
                "password",
                "0762537817",
                "test@test.com");
    }

    public static <T> boolean responseNotNull(ResponseEntity<T> response) {
        if (!response.getStatusCode().is2xxSuccessful() && response.getBody() == null) {
            log.error("Error response status or null body!");
            return false;
        }
        return true;
    }

    public static <T> AbstractBooleanAssert<?> assertResponseNotNull(ResponseEntity<T> actual) {

        Predicate<ResponseEntity<T>> predicate = (response) -> response.getStatusCode().is2xxSuccessful() && response.getBody() != null;

        return AssertionsForInterfaceTypes.assertThat(predicate.test(actual));
    }
    }
