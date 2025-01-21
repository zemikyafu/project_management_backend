package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/company/{companyId}/roles_permissions")
public class CompanyRolePermissionController {
    private final RolePermissionService rolePermissionService;

    public CompanyRolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }


    @Operation(summary = "Find all Role Permissions for a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role Permissions retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<GlobalResponse<List<RolePermission>>> findAllByCompanyId(@PathVariable UUID companyId) {
        List<RolePermission> rolePermissions = rolePermissionService.findAllByCompanyId(companyId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), rolePermissions));
    }
}
