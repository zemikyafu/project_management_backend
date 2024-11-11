package org.project_management.presentation.controllers;

import org.project_management.application.dto.UserDto;
import org.project_management.application.services.AuthService;
import org.project_management.application.services.UserService;
import org.project_management.domain.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;
    private AuthService authService;

   @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody User user) {
        user.setPassword(authService.generateHash((user.getPassword())));
        User savedUser = userService.save(user);
        return ResponseEntity.ok(new UserDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable UUID id) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getEmail(), user.getStatus()));
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getStatus()))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(new UserDto(updatedUser.getId(), user.getName(), user.getEmail(), user.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
