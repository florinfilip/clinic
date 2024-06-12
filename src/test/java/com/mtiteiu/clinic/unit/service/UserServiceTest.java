package com.mtiteiu.clinic.unit.service;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.exception.DatabaseActionException;
import com.mtiteiu.clinic.exception.NotFoundException;
import com.mtiteiu.clinic.model.Person;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.PatientRepository;
import com.mtiteiu.clinic.repository.RoleRepository;
import com.mtiteiu.clinic.repository.UserRepository;
import com.mtiteiu.clinic.service.UserServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.mtiteiu.clinic.Constants.*;
import static com.mtiteiu.clinic.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {
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

    User defaultUser = createDefaultUser();

    UserDTO registrationRequest = createDefaultRegistrationRequest();

    @Test
    void getUsers() {
        //Given
        List<User> userList = createUserList();
        when(userRepository.findAll()).thenReturn(userList);

        //When
        List<User> result = userService.getUsers();

        //Then
        assertThat(userList).isSameAs(result);
    }

    @Test
    void getUsers_shouldThrow() {
        //Given
        when(userRepository.findAll()).thenThrow(RuntimeException.class);

        //When/Then

        try {
            userService.getUsers();
        } catch (RuntimeException ex) {
            assertThat(ex).hasMessage("Could not fetch users from database!");
        }
    }

    @Test
    void getUser() {
        //Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(defaultUser));

        //When
        var result = userService.getUserById(1L);

        //then
        assertThat(result).isEqualTo(defaultUser);
    }

    @Test
    void getUser_ShouldThrow() {
        //Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //When/Then
        Exception ex = assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
        assertThat(ex).hasMessage("User with id 1 not found!");
    }


    @Test
    void loadUserByUsername_UserFound_ReturnsUserDetails() {
        // Given
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(defaultUser));

        // When
        UserDetails userDetails = userService.loadUserByUsername(EMAIL);

        // Then
        assertEquals(defaultUser.getEmail(), userDetails.getUsername());
        verify(userRepository, times(1)).findUserByEmail(EMAIL);
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        // Given
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

        // When/Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(EMAIL));
        assertEquals("No username with value " + EMAIL + " found!", exception.getMessage());

        verify(userRepository, times(1)).findUserByEmail(EMAIL);
    }

    @Test
    void voidCreateUser_ExistingPatient() {
        //given
        Role defaultUserRole = new Role("USER");

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(defaultUserRole));

        Patient patient = Patient.builder()
                .firstName("firstName")
                .lastName("lastName")
                .phoneNumber(registrationRequest.getPhoneNumber())
                .build();

        when(patientRepository.findPatientByPhoneNumber(anyString()))
                .thenReturn(Optional.ofNullable(patient));

        //when/then
        userService.createUser(registrationRequest);
        verify(userRepository).save(userCaptor.capture());

        User createdUser = userCaptor.getValue();
        //then

        assertNotNull(createdUser);
        assertEquals("test@test.com", createdUser.getEmail());
        assertEquals(1, createdUser.getRoles().size());
        assertThat(createdUser.getRoles()).containsExactly(defaultUserRole);
        assertThat(createdUser.getPerson()).isNotNull();
        assertThat(createdUser.getPerson())
                .extracting(Person::getFirstName, Person::getLastName)
                .containsExactly("firstName", "lastName");

        verify(passwordEncoder).encode("password");
        verify(userRepository).save(createdUser);
    }


    @Test
    void voidCreateUser_NoPatient_ShouldSaveAndCreatePatient() {
        //given
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
        assertThat(createdUser.getPerson()).isNotNull();
        assertThat(createdUser.getPerson())
                .extracting(Person::getFirstName, Person::getLastName)
                .containsExactly("firstName", "lastName");

        verify(passwordEncoder).encode("password");
        verify(userRepository).save(createdUser);
    }

    @Test
    void voidCreateUser_passNotMatching_shouldThrow() {
        //given

        var registrationRequest = UserDTO.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .repeatPassword("repeatPassword")
                .lastName("Test")
                .firstName("Test")
                .build();
        Role defaultUserRole = new Role("USER");

        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(defaultUserRole));


        //when/then

        Exception ex = assertThrows(ValidationException.class, () -> userService.createUser(registrationRequest));

        assertThat(ex.getMessage()).isEqualTo("Passwords do not match!");
    }

    @Test
    void createUser_whenDefaultRoleNotFound_shouldCreateDefault() {
        //given
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
    void createUser_shouldThrow() {
        //given

        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
        doThrow(new RuntimeException()).when(userRepository).save(any(User.class));
        //when

        Exception ex = assertThrows(DatabaseActionException.class, () -> userService.createUser(registrationRequest));

        //then
        assertThat(ex.getMessage()).isEqualTo("Error while trying to save user test@test.com: null");
    }

    @Test
    void updateUser_shouldUpdateOnlyUserFields() {
        //Given

        User updateRequestBody = createUser(
                "test@gmail.com",
                "testPassword",
                "04567342132",
                Patient.builder().build());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(defaultUser));
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        //When
        userService.updateUser(1L, updateRequestBody);

        //Then
        verify(userRepository).save(userCaptor.capture());

        User updatedUser = userCaptor.getValue();

        assertThat(updatedUser)
                .extracting(
                        User::getEmail,
                        User::getPassword,
                        User::getEnabled,
                        User::getPerson)
                .containsExactly(updateRequestBody.getEmail(), ENCODED_PASSWORD, true, defaultUser.getPerson());

        assertNotNull(updatedUser.getRoles());
    }

    @Test
    void updateUser_whenUserNotFound_shouldThrow() {

        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when/then

        try {
            userService.updateUser(1L, new User());
        } catch (NotFoundException ex) {
            assertThat(ex).hasMessage("User with id 1 not found!");
        }
    }

    @Test
    void testDeleteUserById_Success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUserById(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserById_Failure() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When/Then
        try {
            userService.deleteUserById(1L);
        } catch (NotFoundException ex) {
            assertThat(ex).hasMessage("User with id 1 does not exist!");
        }
    }
}
