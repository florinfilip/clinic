package com.mtiteiu.clinic.unit.controllers;

import com.mtiteiu.clinic.TestUtils;
import com.mtiteiu.clinic.controllers.UserController;
import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.exception.NotFoundException;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.service.UserService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

import static com.mtiteiu.clinic.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUsers() {
        //given
        List<User> userList = Arrays.asList(
                createUser("email@email.com", "password", "0741625364", new Patient()),
                createUser("test@test.com", "password", "0782634646", new Patient()));
        when(userService.getUsers()).thenReturn(userList);

        //when
        ResponseEntity<List<User>> response = userController.getUsers();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    void getUser() {
        //given
        User user = createDefaultUser();
        when(userService.getUserById(anyLong())).thenReturn(user);

        //when
        ResponseEntity<User> response = userController.getUser(1L);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testCreateUser() {
        //given
        UserDTO userRequest = TestUtils.createDefaultRegistrationRequest();
        User newUser = createDefaultUser();

        //when
        when(userService.createUser(userRequest)).thenReturn(newUser);

        ResponseEntity<User> response = userController.createUser(userRequest);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser, response.getBody());
    }

    @Test
    void testUpdateUser() {
        //given
        User updatedUser = createUpdatedUser();
        UserDTO updatedDetails = createDefaultUserDTO();
        MyUserDetails userDetails = createDefaultMyUserDetails();

        when(userService.updateUser(any(MyUserDetails.class), any(UserDTO.class))).thenReturn(updatedUser);

        //when
        ResponseEntity<User> response = userController.updateUser(userDetails, updatedDetails);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void deleteUser_shouldDelete() {
        //Given
        when(userService.deleteUserById(1L)).thenReturn("User with id 1 deleted successfully!");

        // When
        ResponseEntity<String> response = userController.deleteUser(1L);

        // Then
        verify(userService, times(1)).deleteUserById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("User with id 1 deleted successfully!", response.getBody());
    }

    @Test
    void deleteUser_shouldThrow() {
        //Given
        when(userService.deleteUserById(1L)).thenThrow(NotFoundException.class);

        // When
        assertThrows(NotFoundException.class, () -> userController.deleteUser(1L));
    }

    @Test
    void getUserDetails_Authenticated_ReturnsUserDetails() {
        // given
        MyUserDetails mockUserDetails = createDefaultMyUserDetails();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        // when
        ResponseEntity<UserDetails> response = userController.getUserDetails(authentication);

        // then
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(mockUserDetails, response.getBody());
    }

    @Test
    void getUserDetails_NotAuthenticated_ThrowsValidationException() {
        // given
        when(authentication.isAuthenticated()).thenReturn(false);

        // when
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.getUserDetails(authentication));
        //then
        assertEquals("No authentication session found!", exception.getMessage());
    }

    @Test
    void getUserDetails_NullAuthentication_ThrowsValidationException() {
        // when/then
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.getUserDetails(null));

        assertEquals("No authentication session found!", exception.getMessage());
    }
}
