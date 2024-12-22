package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.project_management.application.dto.user.*;
import org.project_management.application.services.Auth.AuthService;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication management")
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


    @PostMapping("/onBoarding/{invitationId}")
    @Operation(
            summary = "Complete user onboarding",
            description = "Endpoint to complete the onboarding process for a user using the provided invitation ID.",
            parameters = @Parameter(
                    name = "invitationId",
                    description = "Unique ID associated with the invitation",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Onboarding details",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OnBoardingRequest.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Eddy Example\", \"password\": \"#MySuperS3cretP4ssword#\"}")
                    )
            )
    )
    public ResponseEntity<GlobalResponse<OnBoardingResponse>> completeOnBoarding(
            @PathVariable("invitationId") UUID invitationId,
            @RequestBody @Valid OnBoardingRequest onboardingRequest) {
        OnBoardingResponse onBoardingResponse = authService.CompleteOnBoarding(onboardingRequest, invitationId);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), onBoardingResponse), HttpStatus.OK);
    }
}
