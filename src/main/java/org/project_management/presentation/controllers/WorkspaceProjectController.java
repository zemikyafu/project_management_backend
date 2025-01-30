package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.project.ProjectService;
import org.project_management.application.services.workspace.WorkspaceService;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Workspace", description = "Workspace management")
public class WorkspaceProjectController {
    private final WorkspaceService workspaceService;
    private final ProjectService projectService;
    public WorkspaceProjectController(WorkspaceService workspaceService, ProjectService projectService) {
        this.workspaceService = workspaceService;
        this.projectService = projectService;
    }


    @Operation(summary = "Retrieve a specific workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/workspaces/{workspaceId}/")
    @PreAuthorize("hasAuthority('WORKSPACE-READ')")
    public ResponseEntity<GlobalResponse<Workspace>> findWorkspaceById(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId) {
        Workspace workspace = workspaceService.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + workspaceId));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), workspace));
    }
        @GetMapping("/projects/{projectId}")
    @PreAuthorize("hasAuthority('PROJECT-READ')")
    public ResponseEntity<GlobalResponse<Project>> findProjectByProjectId(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId
    ) {
        Project project = projectService.findByIdAndWorkspaceId(projectId, workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), project));
    }
}