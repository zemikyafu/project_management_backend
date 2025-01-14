package org.project_management.presentation.controllers;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.AccessControl.PermissionService;
import org.project_management.domain.entities.permission.Permission;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<Permission>>> findAll() {
        List<Permission> permissions = permissionService.findAll();
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), permissions));
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<GlobalResponse<Permission>> findById(UUID permissionId) {
        Permission permission = permissionService.findById(permissionId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), permission));
    }

}
