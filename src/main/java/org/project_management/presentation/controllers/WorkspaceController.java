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
import org.project_management.application.dto.workspace.WorkspaceCreate;
import org.project_management.application.dto.workspace.WorkspaceUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.workspace.WorkspaceService;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/workspaces")
@Tag(name = "Workspace", description = "Workspace management")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @Operation(summary = "Create a new workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workspace created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to save workspace")
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('WORKSPACE-CREATE')")
    public ResponseEntity<GlobalResponse<Workspace>> saveWorkspace(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New workspace information", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkspaceCreate.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Workspace A\", \"description\": \"Description of Workspace A\", \"companyId\": \"123e4567-e89b-12d3-a456-426614174000\" }")
                    ))
            @RequestBody @Valid WorkspaceCreate newWorkspace) {
        Workspace savedWorkspace = workspaceService.save(newWorkspace);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), savedWorkspace), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve all workspaces for a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspaces retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/")
    @PreAuthorize("hasAuthority('WORKSPACE-READ')")
    public ResponseEntity<GlobalResponse<List<Workspace>>> findAllWorkspaces(
            @Parameter(description = "Company ID", required = true) @PathVariable UUID companyId) {
        List<Workspace> workspaces = workspaceService.findByCompanyId(companyId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), workspaces));
    }

    @Operation(summary = "Retrieve a specific workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{workspaceId}")
    @PreAuthorize("hasAuthority('WORKSPACE-READ')")
    public ResponseEntity<GlobalResponse<Workspace>> findWorkspaceById(
            @Parameter(description = "Company ID", required = true) @PathVariable UUID companyId,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId) {
        Workspace workspace = workspaceService.findByIdAndCompanyId(workspaceId, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), workspace));
    }

    @Operation(summary = "Update a specific workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{workspaceId}")
    @PreAuthorize("hasAuthority('WORKSPACE-UPDATE')")
    public ResponseEntity<GlobalResponse<Workspace>> updateWorkspace(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated workspace data", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkspaceUpdate.class),
                            examples = @ExampleObject(value = "{ \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Updated Workspace\", \"description\": \"Updated Description\" }")
                    ))
            @Valid @RequestBody WorkspaceUpdate updateDto,
            @Parameter(description = "Company ID", required = true) @PathVariable UUID companyId,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId) {
        updateDto.setId(workspaceId);
        Workspace updatedWorkspace = workspaceService.update(updateDto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedWorkspace));
    }

    @Operation(summary = "Delete a specific workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{workspaceId}")
    @PreAuthorize("hasAuthority('WORKSPACE-DELETE')")
    public ResponseEntity<GlobalResponse<String>> deleteWorkspace(
            @Parameter(description = "Company ID", required = true) @PathVariable UUID companyId,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId) {
        workspaceService.deleteByIdAndCompanyId(workspaceId, companyId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), "Workspace deleted successfully"));
    }
}