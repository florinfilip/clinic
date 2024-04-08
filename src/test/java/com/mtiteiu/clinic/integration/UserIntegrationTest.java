package com.mtiteiu.clinic.integration;

import com.mtiteiu.clinic.controllers.UserController;
import com.mtiteiu.clinic.dao.UserRegistrationRequest;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.UserRepository;
import com.mtiteiu.clinic.security.JwtUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static com.mtiteiu.clinic.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CREATED;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public class UserIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @LocalServerPort
    int port;

    @MockBean
    JwtUtils jwtUtils;
    @Container
    private static final OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withUsername("test")
            .withPassword("password");

    @BeforeAll
    static void beforeAll() {
        oracleContainer.start();
    }

    @Test
    void createUser() {

        //given

        UserRegistrationRequest body = UserRegistrationRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .email(EMAIL)
                .build();

        PatientDetails expectedPatientDetails = PatientDetails.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .phoneNumber(PHONE_NUMBER)
                .build();
        User expected = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .enabled(true)
                .patient(expectedPatientDetails)
                .roles(Set.of(new Role("USER"))).
                build();

        //when
        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:" + port + "/users/add", body, User.class);

        //then
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        var optionalUser = userRepository.findUserByEmail(response.getBody().getEmail());

        assertThat(optionalUser.isPresent()).isTrue();

        var actual = optionalUser.get();

        assertThat(actual)
                .extracting(User::getEmail,
                        User::getPhoneNumber,
                        User::getEnabled)
                .containsExactly(expected.getEmail(),
                        expected.getPhoneNumber(),
                        expected.getEnabled());

        assertThat(passwordEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();

        assertThat(actual.getPatient()).isNotNull();
        assertThat(actual.getPatient())
                .extracting(PatientDetails::getFirstName,
                        PatientDetails::getLastName,
                        PatientDetails::getPhoneNumber)
                .contains(expectedPatientDetails.getFirstName(),
                        expectedPatientDetails.getLastName(),
                        expectedPatientDetails.getPhoneNumber());

    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", oracleContainer::getJdbcUrl);
        registry.add("spring.datasource.username", oracleContainer::getUsername);
        registry.add("spring.datasource.password", oracleContainer::getPassword);
    }

    @AfterAll
    static void tearDown() {
        oracleContainer.stop();
    }


}