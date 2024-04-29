package com.mtiteiu.clinic.controllers;

import com.mtiteiu.clinic.security.AuthRequest;
import com.mtiteiu.clinic.security.JwtUtils;
import com.mtiteiu.clinic.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final UserService userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {

        ResponseEntity<String> response = null;
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            final UserDetails user = userDetailsService.loadUserByUsername(authRequest.getEmail());
            if (user != null) {
                return ResponseEntity.ok(jwtUtils.generateToken(user));
            }
        } catch (Exception ex){
            log.error("Error while trying to authenticate user! {}", ex.getMessage());
            response = ResponseEntity.status(400).body(String.format("Some error has occurred while trying to authenticate the user! %s", ex.getMessage()));
        }
        return response;
    }
}
