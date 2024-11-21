package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "409", description = "User already exists with provided email"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to save user")
    })
    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<UserRead>> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "New user information", required = true,
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = SignupRequest.class),
                        examples = @ExampleObject(value = "{ \"name\": \"Eddy Example\", \"email\": \"eddy.example@gmail.com\", \"password\": \"#MySuperS3cretP4ssword#\" }")
                ))
            @RequestBody @Valid SignupRequest signupRequest) {
        User user = UserMapper.toUser(signupRequest);
        User savedUser = authService.signUp(user);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), UserMapper.toUserRead(savedUser)), HttpStatus.CREATED);
    }

    @Operation(summary = "Sign in with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided"),
            @ApiResponse(responseCode = "404", description = "User not found with provided email"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to sign in")
    })
    @PostMapping("/signin")
    public ResponseEntity<GlobalResponse<SigninResponse>> signIn(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Existing user credentials", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SigninRequest.class),
                            examples = @ExampleObject(value = "{ \"email\": \"eddy.example@gmail.com\", \"password\": \"#MySuperS3cretP4ssword#\" }")
                    ))
            @RequestBody @Valid SigninRequest signinRequest) {
        SigninResponse signinResponse = authService.signIn(signinRequest.getEmail(), signinRequest.getPassword());
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), signinResponse), HttpStatus.OK);
    }
}
