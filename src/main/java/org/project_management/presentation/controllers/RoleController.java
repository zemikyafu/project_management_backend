package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.project_management.application.dto.role.RoleCreate;
import org.project_management.application.dto.role.RoleUpdate;
import org.project_management.application.services.AccessControl.RoleService;
import org.project_management.domain.entities.role.Role;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Role", description = "Role management API endpoints")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Create a new role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Role with the same name already exists"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE-CREATE')")
    public ResponseEntity<GlobalResponse<Role>> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details for creating a role",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleCreate.class))
            )
            @RequestBody @Valid RoleCreate roleCreateDto) {
        Role role = roleService.save(roleCreateDto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), role), HttpStatus.CREATED);
    }

    @Operation(summary = "Find a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE-READ')")
    public ResponseEntity<GlobalResponse<Role>> findById(
            @Parameter(description = "Role ID", required = true) @PathVariable UUID id) {
        Role role = roleService.findById(id);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), role));
    }

    @Operation(summary = "Find a role by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('ROLE-READ')")
    public ResponseEntity<GlobalResponse<Role>> findByName(
            @Parameter(description = "Role name", required = true) @PathVariable String name) {
        Role role = roleService.findByName(name);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), role));
    }

    @Operation(summary = "Find all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE-READ-ALL')")
    public ResponseEntity<GlobalResponse<List<Role>>> findAll() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), roles));
    }

    @Operation(summary = "Find roles by company ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('ROLE-READ')")
    public ResponseEntity<GlobalResponse<List<Role>>> findByCompanyId(
            @Parameter(description = "Company ID", required = true) @PathVariable UUID companyId) {
        List<Role> roles = roleService.findByCompanyId(companyId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), roles));
    }

    @Operation(summary = "Update an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "404", description = "Role or company not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE-UPDATE')")
    public ResponseEntity<GlobalResponse<Role>> update(
            @Parameter(description = "Role ID", required = true) @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details for updating a role",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleUpdate.class))
            )
            @RequestBody @Valid RoleUpdate roleUpdateDto) {
        roleUpdateDto.setId(id); // Ensure the ID in the path matches the DTO
        Role updatedRole = roleService.update(roleUpdateDto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedRole));
    }

    @Operation(summary = "Delete a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE-DELETE')")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Role ID", required = true) @PathVariable UUID id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
