package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.project_management.application.dto.role.RolePermissionCreate;
import org.project_management.application.dto.role.RolePermissionUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.AccessControl.RolePermissionService;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.role.RolePermissionId;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles/{roleId}/permissions")
public class RolePermissionController {
    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @Operation(summary = "Find a Role Permission by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role Permission retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Role Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{permissionId}")
    public ResponseEntity<GlobalResponse<RolePermission>> findById(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        RolePermissionId rolePermissionId = new RolePermissionId(roleId, permissionId);
        RolePermission rolePermission = rolePermissionService.findById(rolePermissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Role Permission not found"));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), rolePermission));
    }

    @Operation(summary = "Create a new Role Permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role Permission created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<GlobalResponse<RolePermission>> create(
            @RequestBody @Valid RolePermissionCreate rolePermissionCreate) {
        RolePermission createdRolePermission = rolePermissionService.save(rolePermissionCreate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(HttpStatus.CREATED.value(), createdRolePermission));
    }

    @Operation(summary = "Update a Role Permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role Permission updated successfully"),
            @ApiResponse(responseCode = "404", description = "Role Permission not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{permissionId}")
    public ResponseEntity<GlobalResponse<RolePermission>> update(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId,
            @RequestBody @Valid RolePermissionUpdate rolePermissionUpdate) {
        rolePermissionUpdate.setId(new RolePermissionId(roleId, permissionId));
        RolePermission updatedRolePermission = rolePermissionService.update(rolePermissionUpdate);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedRolePermission));
    }

    @Operation(summary = "Delete a Role Permission by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role Permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role Permission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        rolePermissionService.delete(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find all Role Permissions for a Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role Permissions retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<GlobalResponse<List<RolePermission>>> findAllByRoleId(@PathVariable UUID roleId) {
        List<RolePermission> rolePermissions = rolePermissionService.findAllByRoleId(roleId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), rolePermissions));
    }
}
