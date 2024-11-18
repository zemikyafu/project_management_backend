package org.project_management.presentation.controllers;

import jakarta.validation.Valid;
import org.project_management.application.dto.User.*;
import org.project_management.application.services.AuthService;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<UserRead>> save(@RequestBody @Valid SignupRequest signupRequest) {
        User user = UserMapper.toUser(signupRequest);
        User savedUser = authService.signUp(user);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), UserMapper.toUserRead(savedUser)), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<GlobalResponse<SigninResponse>> signIn(@RequestBody @Valid SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signIn(signinRequest.getEmail(), signinRequest.getPassword());
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), signinResponse), HttpStatus.CREATED);
    }
}
