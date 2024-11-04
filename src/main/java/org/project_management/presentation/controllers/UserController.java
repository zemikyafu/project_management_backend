package org.project_management.presentation.controllers;

import org.project_management.application.dto.UserDto;
import org.project_management.application.services.UserService;
import org.project_management.domain.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Api/v1/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> save(User user) {
        userService.save(user);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getEmail(), user.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable int id) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getEmail(), user.getStatus()));
    }

}
