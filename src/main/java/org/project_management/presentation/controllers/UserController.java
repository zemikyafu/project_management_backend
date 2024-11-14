package org.project_management.presentation.controllers;

import jakarta.validation.Valid;
import org.project_management.application.dto.User.*;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.AuthService;
import org.project_management.application.services.UserService;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<UserRead>> save(@RequestBody @Valid UserCreate userCreate) {
        User user = UserMapper.toUser(userCreate);
        user.setPassword(authService.generateHash(userCreate.getPassword()));

        User savedUser = userService.save(user);

        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), UserMapper.toUserRead(savedUser)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<UserRead>> findById(@PathVariable UUID id) {
        User user = userService.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), UserMapper.toUserRead(user)));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<UserRead>>> findAll() {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), userService.findAll().stream().map(UserMapper::toUserRead).collect(Collectors.toList())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<UserRead>> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdate userUpdate) {
        User user = UserMapper.toUser(userUpdate);
        user.setId(id);

        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.CREATED.value(), UserMapper.toUserRead(updatedUser)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<UserRead>> updateUserAndEmail(@PathVariable UUID id, @Valid @RequestBody UserPartialUpdate userUpdateDto) {
        User user = UserMapper.toUser(userUpdateDto);
        user.setId(id);

        User updatedUser = userService.updateNameOrEmail(user);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.CREATED.value(), UserMapper.toUserRead(updatedUser)));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        GlobalResponse<String> response = new GlobalResponse<>(HttpStatus.OK.value(), "User deleted successfully");
        return ResponseEntity.ok(response);
    }
}
