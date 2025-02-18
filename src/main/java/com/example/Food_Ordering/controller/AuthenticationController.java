package com.example.Food_Ordering.controller;

import com.example.Food_Ordering.dto.AuthenticationRequest;
import com.example.Food_Ordering.dto.AuthenticationResponse;
import com.example.Food_Ordering.dto.UserDTO;
import com.example.Food_Ordering.service.AuthenticationService;
import com.example.Food_Ordering.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.rmi.server.LogStream.log;
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    //  creating user with address and sending jwt token
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody UserDTO userDTO) {
        log("I am in login with given request body");
        AuthenticationResponse response = authenticationService.register(userDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // login user and sending the jwt token
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    // Generating the refresh token
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(authenticationRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
    }

    @PostMapping("/log-out")
    public ResponseEntity<AuthenticationResponse> logOut(@RequestBody AuthenticationRequest authenticationRequest){
        System.out.println("I am in log out controller");
        AuthenticationResponse authenticationResponse = authenticationService.logOutUser(authenticationRequest);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

}
