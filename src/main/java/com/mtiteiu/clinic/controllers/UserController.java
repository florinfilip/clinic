package com.mtiteiu.clinic.controllers;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("/principal")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDetails> getUserDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("No authentication session found!");
        }

        UserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(userDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userService.deleteUserById(id));
    }

}
