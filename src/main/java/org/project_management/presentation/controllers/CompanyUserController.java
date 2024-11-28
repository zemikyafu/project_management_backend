package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.project_management.application.dto.company.CompanyUserCreate;
import org.project_management.application.dto.company.CompanyUserUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.Company.CompanyUserService;
import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.company.CompanyUserId;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/users")
public class CompanyUserController {

    private final CompanyUserService companyUserService;

    public CompanyUserController(CompanyUserService companyUserService) {
        this.companyUserService = companyUserService;
    }

    @Operation(summary = "Create a new Company User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User or Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('COMPANYUSER-CREATE')")
    public ResponseEntity<GlobalResponse<CompanyUser>> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details for creating a Company User",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyUserCreate.class))
            )
            @RequestBody @Valid CompanyUserCreate companyUserCreate,
            @PathVariable UUID companyId) {
        companyUserCreate.setCompanyId(companyId); // Ensure the company ID is set
        CompanyUser companyUser = companyUserService.save(companyUserCreate);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), companyUser), HttpStatus.CREATED);
    }

    @Operation(summary = "Find a Company User by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Company User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('COMPANYUSER-READ')")
    public ResponseEntity<GlobalResponse<CompanyUser>> findById(
            @PathVariable UUID companyId,
            @PathVariable UUID userId) {
        CompanyUserId companyUserId = new CompanyUserId(userId, companyId);
        CompanyUser companyUser = companyUserService.findById(companyUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Company User not found"));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), companyUser));
    }

    @Operation(summary = "Find all Company Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('COMPANYUSER-READ-ALL')")
    public ResponseEntity<GlobalResponse<List<CompanyUser>>> findAll(@PathVariable UUID companyId) {
        List<CompanyUser> companyUsers = companyUserService.findAllByCompanyId(companyId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), companyUsers));
    }

    @Operation(summary = "Update a Company User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company User updated successfully"),
            @ApiResponse(responseCode = "404", description = "Company User, User, or Company not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('COMPANYUSER-UPDATE')")
    public ResponseEntity<GlobalResponse<CompanyUser>> update(
            @PathVariable UUID companyId,
            @PathVariable UUID userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details for updating a Company User",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyUserUpdate.class))
            )
            @RequestBody @Valid CompanyUserUpdate companyUserUpdate) {
        companyUserUpdate.setId(new CompanyUserId(userId, companyId));
        CompanyUser updatedCompanyUser = companyUserService.update(companyUserUpdate);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedCompanyUser));
    }

    @Operation(summary = "Delete a Company User by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Company User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Company User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('COMPANYUSER-DELETE')")
    public ResponseEntity<Void> deleteById(
            @PathVariable UUID companyId,
            @PathVariable UUID userId) {
        companyUserService.delete(userId, companyId);
        return ResponseEntity.noContent().build();
    }
}