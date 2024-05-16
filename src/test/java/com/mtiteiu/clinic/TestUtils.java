package com.mtiteiu.clinic;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.patient.*;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.mtiteiu.clinic.Constants.*;

@Slf4j
public class TestUtils {

    public static User createUser(String email, String password, String phoneNumber, Patient patient) {
        return User.builder()
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .enabled(true)
                .roles(Set.of(new Role("USER")))
                .person(patient)
                .build();
    }

    public static User createDefaultUser() {
        return createUser(PASSWORD,
                EMAIL,
                PHONE_NUMBER,
                Patient.builder()
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


    public static Patient createDefaultPatient() {
        return Patient.builder()
                .id(1L)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .cnp(CNP)
                .phoneNumber(PHONE_NUMBER)
                .patientDetails(createDefaultPatientDetails())
                .build();
    }

    public static Patient createPatient(Long id, String cnp, String firstName, String lastName, String phoneNumber) {
        return Patient.builder()
                .id(id)
                .cnp(cnp)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static List<Patient> createDefaultPatientList() {
        return List.of(
                createPatient(1L, "1900312236329", "John", "Doe", "123456790"),
                createPatient(2L, "1900312230301", "Harry", "Potter", "987654421"),
                createPatient(3L, "1900312232057", "Hermione", "Granger", "8179263312"),
                createPatient(4L, "1900312239543", "Severus", "Snake", "123124123")
        );
    }

    public static PatientDetails createDefaultPatientDetails() {
        return PatientDetails.builder()
                .patient(Patient.builder()
                        .id(1L)
                        .phoneNumber(PHONE_NUMBER)
                        .dateOfBirth(LocalDate.of(1999, 12, 15))
                        .build())
                .id(1L)
                .age(20)
                .allergies(Collections.emptyList())
                .gender(Gender.MALE)
                .bloodType(BloodType.AB_NEGATIVE)
                .race(Race.HISPANIC)
                .religion(Religion.ORTODOX)
                .height(12.3)
                .weight(59.3)
                .build();
    }

    public static PatientDetails createPatientDetails(Integer age, Gender gender, BloodType bloodType, Race race, Religion religion, Double height, Double weight) {
        return PatientDetails.builder()
                .age(age)
                .allergies(Collections.emptyList())
                .gender(gender)
                .bloodType(bloodType)
                .race(race)
                .religion(religion)
                .height(height)
                .weight(weight)
                .build();
    }
}
