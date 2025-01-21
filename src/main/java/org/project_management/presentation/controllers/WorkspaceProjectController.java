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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces")
@Tag(name = "Workspace", description = "Workspace management")
public class WorkspaceProjectController {
    private final WorkspaceService workspaceService;

    public WorkspaceProjectController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }


    @Operation(summary = "Retrieve a specific workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{workspaceId}/")
    @PreAuthorize("hasAuthority('WORKSPACE-READ')")
    public ResponseEntity<GlobalResponse<Workspace>> findWorkspaceById(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId) {
        Workspace workspace = workspaceService.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), workspace));
    }
}