package com.mtiteiu.clinic;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.patient.*;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.mtiteiu.clinic.Constants.*;

@Slf4j
public class TestUtils {

    public static User createUser(String email, String password, String phoneNumber, Patient patient) {
        return User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .phoneNumber(phoneNumber)
                .enabled(true)
                .roles(Set.of(new Role("USER")))
                .person(patient)
                .build();
    }

    public static MyUserDetails createDefaultMyUserDetails() {
        return new MyUserDetails(createDefaultUser());
    }

    public static User createDefaultUser() {
        return createUser(
                EMAIL,
                PASSWORD,
                PHONE_NUMBER,
                Patient.builder()
                        .user(createUser(EMAIL, PASSWORD, PHONE_NUMBER, createDefaultPatient()))
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .phoneNumber(PHONE_NUMBER)
                        .patientDetails(createDefaultPatientDetails())
                        .build());
    }

    public static User createUpdatedUser() {
        return createUser(
                UPDATED_EMAIL,
                UPDATED_PASSWORD,
                UPDATED_PHONE_NO,
                Patient.builder()
                        .user(createUser(UPDATED_EMAIL, UPDATED_PASSWORD, UPDATED_PHONE_NO, createDefaultPatient()))
                        .firstName(UPDATED_FIRST_NAME)
                        .lastName(UPDATED_LAST_NAME)
                        .phoneNumber(UPDATED_PHONE_NO)
                        .patientDetails(createDefaultPatientDetails())
                        .build());
    }

    public static List<User> createUserList() {
        return List.of(createDefaultUser(), createDefaultUser(), createDefaultUser());
    }

    public static UserDTO createUserDTO(String username, String lastName, String password, String rPassword, String phoneNumber, String email) {
        return UserDTO.builder()
                .firstName(username)
                .lastName(lastName)
                .cnp(CNP)
                .newPassword(password)
                .confirmPassword(rPassword)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();
    }

    public static UserDTO createDefaultUserDTO(){
        return UserDTO.builder()
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .cnp(UPDATED_CNP)
                .newPassword(UPDATED_PASSWORD)
                .confirmPassword(UPDATED_PASSWORD)
                .phoneNumber(UPDATED_PHONE_NO)
                .email(UPDATED_EMAIL)
                .build();
    }

    public static UserDTO createDefaultRegistrationRequest() {
        return createUserDTO(
                "firstName",
                "lastName",
                "password",
                "password",
                "0762537817",
                "test@test.com");
    }

    public static <T> AbstractBooleanAssert<?> assertResponseNotNull(ResponseEntity<T> actual) {

        Predicate<ResponseEntity<T>> predicate = (response) -> response.getStatusCode().is2xxSuccessful() && response.getBody() != null;

        return AssertionsForInterfaceTypes.assertThat(predicate.test(actual));
    }


    public static Patient createDefaultPatient() {
        return Patient.builder()
                .id(1L)
                .dateOfBirth(LocalDate.of(1999, 3, 3))
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
                .allergies("ASD")
                .bloodType(BloodType.AB_NEGATIVE)
                .race(Race.HISPANIC)
                .religion(Religion.ORTODOX)
                .height(12.3)
                .weight(59.3)
                .build();
    }

    public static PatientDetails createPatientDetails(Integer age, BloodType bloodType, Race race, Religion religion, Double height, Double weight) {
        return PatientDetails.builder()
                .age(age)
                .allergies("asd")
                .bloodType(bloodType)
                .race(race)
                .religion(religion)
                .height(height)
                .weight(weight)
                .build();
    }
}
