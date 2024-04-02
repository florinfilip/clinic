package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.NotFoundException;
import com.mtiteiu.clinic.dao.UserRegistrationRequest;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.PatientRepository;
import com.mtiteiu.clinic.repository.RoleRepository;
import com.mtiteiu.clinic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.mtiteiu.clinic.TestUtils.createDefaultRegistrationRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test
    void voidCreateUser_ExistingPatient() {
        //given
        UserRegistrationRequest registrationRequest = createDefaultRegistrationRequest();

        Role defaultUserRole = new Role("USER");

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(defaultUserRole));

        PatientDetails patientDetails = PatientDetails.builder()
                .firstName("firstName")
                .lastName("lastName")
                .phoneNumber(registrationRequest.getPhoneNumber())
                .build();

        when(patientRepository.findPatientByPhoneNumber(anyString()))
                .thenReturn(Optional.ofNullable(patientDetails));

        //when/then
        userService.createUser(registrationRequest);
        verify(userRepository).save(userCaptor.capture());

        User createdUser = userCaptor.getValue();
        //then

        assertNotNull(createdUser);
        assertEquals("test@test.com", createdUser.getEmail());
        assertEquals(1, createdUser.getRoles().size());
        assertThat(createdUser.getRoles()).containsExactly(defaultUserRole);
        assertThat(createdUser.getPatient()).isNotNull();
        assertThat(createdUser.getPatient())
                .extracting(PatientDetails::getFirstName, PatientDetails::getLastName)
                .containsExactly("firstName", "lastName");

        verify(passwordEncoder).encode("password");
        verify(userRepository).save(createdUser);
    }


    @Test
    void voidCreateUser_NoPatient_ShouldSaveAndCreatePatient() {
        //given
        UserRegistrationRequest registrationRequest = createDefaultRegistrationRequest();
        Role defaultUserRole = new Role("USER");

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(defaultUserRole));

        when(patientRepository.findPatientByPhoneNumber(anyString()))
                .thenReturn(Optional.empty());

        //when/then
        userService.createUser(registrationRequest);
        verify(userRepository).save(userCaptor.capture());

        User createdUser = userCaptor.getValue();
        //then

        assertNotNull(createdUser);
        assertEquals("test@test.com", createdUser.getEmail());
        assertEquals(1, createdUser.getRoles().size());
        assertThat(createdUser.getRoles()).containsExactly(defaultUserRole);
        assertThat(createdUser.getPatient()).isNotNull();
        assertThat(createdUser.getPatient())
                .extracting(PatientDetails::getFirstName, PatientDetails::getLastName)
                .containsExactly("firstName", "lastName");

        verify(passwordEncoder).encode("password");
        verify(userRepository).save(createdUser);
    }

    @Test
    void createUser_WhenDefaultRoleNotFound_ShouldCreateDefault() {
        //given
        UserRegistrationRequest registrationRequest = createDefaultRegistrationRequest();

        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(patientRepository.findPatientByPhoneNumber(anyString()))
                .thenReturn(Optional.empty());

        //when

        userService.createUser(registrationRequest);
        verify(userRepository).save(userCaptor.capture());

        User createdUser = userCaptor.getValue();
        //then
        assertEquals(1, createdUser.getRoles().size());
        assertThat(createdUser.getRoles().stream()
                .map(Role::getName)
                .toList())
                .containsExactly("USER");
        verify(roleRepository).findByName("USER");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(createdUser);
    }

    @Test
    void createUser_ShouldThrow() {
        //given
        UserRegistrationRequest registrationRequest = createDefaultRegistrationRequest();

        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        doThrow(new RuntimeException()).when(userRepository).save(any(User.class));
        //when

        Exception ex = assertThrows(NotFoundException.class, () -> userService.createUser(registrationRequest));

        //then
        assertThat(ex.getMessage()).isEqualTo("Error while trying to save user test@test.com: null");
    }
}
