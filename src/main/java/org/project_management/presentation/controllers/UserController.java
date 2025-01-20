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
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.Auth.AuthService;
import org.project_management.application.services.User.UserService;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User management")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(summary = "Get existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "User not found with provided ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to find user")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@securityUtils.isOwner(#id)")
    public ResponseEntity<GlobalResponse<UserRead>> findById(
            @Parameter(description = "User id", required = true, example = "47ceb1af-94e6-436b-9f43-91cbb6fb2120")
            @PathVariable UUID id
    ) {
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), UserMapper.toUserRead(user)));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<GlobalResponse<List<UserRead>>>findComapanyUsers(@PathVariable UUID id) {
        List<UserRead> userReads = List.of();
        userService.findCompanyUsers(id).stream().map( UserMapper::toUserRead).forEach(userReads::add);
        GlobalResponse<List<UserRead>> response = new GlobalResponse<>(HttpStatus.OK.value(), userReads);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all users which found in owner logged in user company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "No users found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to find users")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('USER-READ')")
    public ResponseEntity<GlobalResponse<List<UserRead>>> findAll() {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), userService.findAll().stream().map(UserMapper::toUserRead).toList()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<GlobalResponse<UserRead>> updateUserStatus(@PathVariable UUID id, @Valid @RequestBody UpdateUserStatusRequest request) {
        User user = userService.updateStatus(request);
        UserRead userRead = new UserRead(user.getId(), user.getName(), user.getEmail(), user.getStatus());
        GlobalResponse<UserRead> response = new GlobalResponse<>(HttpStatus.OK.value(), userRead);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to update user")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@securityUtils.isOwner(#id)")
    public ResponseEntity<GlobalResponse<UserRead>> updateUser(
            @Parameter(description = "User id", required = true, example = "47ceb1af-94e6-436b-9f43-91cbb6fb2120")
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to be updated", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdate.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Eddy Example\", \"email\": \"eddy.example@gmail.com\", \"password\": \"#MyNewSuperS3cretP4ssword#\", \"status\": \"ACTIVE\" }")
                    ))
            @Valid @RequestBody UserUpdate userUpdate
    ) {
        User user = UserMapper.toUser(userUpdate);
        user.setId(id);

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), UserMapper.toUserRead(updatedUser)));
    }

    @Operation(summary = "Partially update existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to update user")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("@securityUtils.isOwner(#id)")
    public ResponseEntity<GlobalResponse<UserRead>> updateUserAndEmail(
            @Parameter(description = "User id", required = true, example = "47ceb1af-94e6-436b-9f43-91cbb6fb2120")
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to be updated", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserPartialUpdate.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Eddy Example\", \"email\": \"eddy.example@gmail.com\" }")
                    ))
            @Valid @RequestBody UserPartialUpdate userUpdateDto
    ) {
        System.out.println("user id from path variable " + id);
        User user = UserMapper.toUser(userUpdateDto);
        System.out.println(user.getEmail() + " " + user.getName());

        user.setId(id);

        User updatedUser = userService.updateNameOrEmail(user);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), UserMapper.toUserRead(updatedUser)));
    }

    @Operation(summary = "Delete existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "User not found with provided ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to delete user")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER-DELETE')")
    public ResponseEntity<GlobalResponse<String>> deleteUser(
            @Parameter(description = "User id", required = true, example = "47ceb1af-94e6-436b-9f43-91cbb6fb2120")
            @PathVariable UUID id
    ) {
        userService.deleteUser(id);

        GlobalResponse<String> response = new GlobalResponse<>(HttpStatus.OK.value(), "User deleted successfully");
        return ResponseEntity.ok(response);
    }
}
