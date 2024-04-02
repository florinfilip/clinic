package com.mtiteiu.clinic.controllers;

import com.mtiteiu.clinic.TestUtils;
import com.mtiteiu.clinic.dao.UserRegistrationRequest;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.mtiteiu.clinic.TestUtils.createDefaultUser;
import static com.mtiteiu.clinic.TestUtils.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUsers() {
        //given
        List<User> userList = Arrays.asList(
                createUser("email@email.com", "password", "0741625364"),
                createUser("test@test.com", "password", "0782634646"));
        when(userService.getUsers()).thenReturn(userList);

        //when
        ResponseEntity<List<User>> response = userController.getUsers();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    void testCreateUser() {
        //given
        UserRegistrationRequest userRequest = TestUtils.createDefaultRegistrationRequest();
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
        User updatedUser = createDefaultUser();

        when(userService.updateUser(any(User.class))).thenReturn(updatedUser);

        //when
        ResponseEntity<User> response = userController.updateUser(createDefaultUser());

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }
}
