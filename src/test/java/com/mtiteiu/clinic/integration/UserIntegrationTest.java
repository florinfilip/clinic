package com.mtiteiu.clinic.integration;

import com.mtiteiu.clinic.controllers.UserController;
import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.patient.Patient;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static com.mtiteiu.clinic.Constants.*;
import static com.mtiteiu.clinic.TestUtils.assertResponseNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.CREATED;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Sql(scripts = {"classpath:insert-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class UserIntegrationTest {

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

    public final String URL = "http://localhost:%s/users/%s";

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
        //Given
        UserDTO body = UserDTO.builder()
                .cnp(CNP)
                .firstName("John")
                .lastName("Doe")
                .password(PASSWORD)
                .repeatPassword(PASSWORD)
                .dateOfBirth(LocalDate.of(1967, 4, 12))
                .phoneNumber("0123123123")
                .email("email@test.com")
                .build();

        //When
        ResponseEntity<User> response = restTemplate.postForEntity(String.format(URL, port, "/add"), body, User.class);

        //Then
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        var responseBody = response.getBody();
        assertNotNull(responseBody.getPatient());
        assertThat(responseBody.getPatient())
                .extracting(
                        Patient::getCnp,
                        Patient::getPhoneNumber,
                        Patient::getFirstName,
                        Patient::getLastName,
                        Patient::getDateOfBirth)
                .containsExactly(
                        CNP,
                        "0123123123",
                        "John",
                        "Doe",
                        LocalDate.of(1967, 4, 12)
                );
        assertThat(responseBody)
                .extracting(
                        User::getEmail,
                        User::getEnabled,
                        User::getPhoneNumber)
                .containsExactly("email@test.com", true, "0123123123");

        var roles = response.getBody().getRoles().stream()
                .map(Role::getName)
                .toList();

        assertThat(roles).containsExactly("USER");
    }

    @Test
    void retrieveUser() {
        //When
        ResponseEntity<User> response = restTemplate.getForEntity(String.format(URL, port, 2L), User.class);

        //Then
        assertNotNull(response.getBody());

        var actual = response.getBody();

        assertThat(actual)
                .extracting(
                        User::getEmail,
                        User::getPhoneNumber,
                        User::getEnabled)
                .containsExactly(
                        "test2@email.com",
                        "0712312323",
                        true);

        assertResponseNotNull(response);

        assertThat(actual.getPatient()).isNotNull();
        assertThat(actual.getPatient())
                .extracting(
                        Patient::getFirstName,
                        Patient::getLastName,
                        Patient::getPhoneNumber)
                .contains(
                        "Popescu",
                        "Ion",
                        "0712312323");

    }

    @Test
    void retrieveAllUsers() {
        //When
        var response = restTemplate.exchange(
                String.format("http://localhost:%s/users", port),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                });
        //Then
        assertNotNull(response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void updateUser() {
        //Given
        var updateDetails = User.builder()
                .email(UPDATED_EMAIL)
                .password(UPDATED_PASSWORD)
                .phoneNumber(UPDATED_PHONE_NO)
                .build();

        HttpEntity<User> requestEntity = new HttpEntity<>(updateDetails, new HttpHeaders());
        //When
        var response = restTemplate.exchange(
                String.format(URL, port, 1L),
                HttpMethod.PUT,
                requestEntity,
                User.class);

        //Then

        assertNotNull(response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
        var user = response.getBody();
        assertThat(user)
                .extracting(User::getEmail, User::getPhoneNumber)
                .containsExactly(UPDATED_EMAIL, UPDATED_PHONE_NO);
        assertTrue(passwordEncoder.matches(UPDATED_PASSWORD, user.getPassword()));
    }

    @Test
    void deleteUser() {
        //When
        restTemplate.delete(String.format(URL, port, 1L));

        //Then
        assertEquals(3, userRepository.findAll().size());
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