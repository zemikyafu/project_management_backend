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
import org.project_management.application.dto.project.ProjectCreate;
import org.project_management.application.dto.project.ProjectUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.project.ProjectService;
import org.project_management.domain.entities.project.Project;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/workspaces/{workspaceId}/projects")
@Tag(name = "Project", description = "Project management")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to save project")
    })
    @PostMapping("/")
    public ResponseEntity<GlobalResponse<Project>> saveProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New project information", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectCreate.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Project A\", \"description\": \"Description of Project A\", \"status\": \"IN_PROGRESS\", \"workspaceId\": \"123e4567-e89b-12d3-a456-426614174000\", \"startDate\": \"2024-01-01\", \"endDate\": \"2024-12-31\" }")
                    ))
            @RequestBody @Valid ProjectCreate newProject) {
        Project savedProject = projectService.save(newProject);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), savedProject), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve all projects in a workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/")
    public ResponseEntity<GlobalResponse<List<Project>>> findAllProjects(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), projectService.findByWorkspaceId(workspaceId)));
    }

    @Operation(summary = "Retrieve a specific project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<GlobalResponse<Project>> findProjectById(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId
    ) {
        Project project = projectService.findByIdAndWorkspaceId(projectId, workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + projectId));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), project));
    }


    @Operation(summary = "Update a specific project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project or workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{projectId}")
    public ResponseEntity<GlobalResponse<Project>> updateProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated project data", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectUpdate.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Updated Project\", \"description\": \"Updated description\", \"status\": \"COMPLETED\", \"startDate\": \"2024-01-01\", \"endDate\": \"2024-06-30\" }")
                    ))
            @Valid @RequestBody ProjectUpdate updateDto,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId
    ) {
        updateDto.setId(projectId);
        Project updatedProject = projectService.update(updateDto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedProject));
    }


    @Operation(summary = "Delete a specific project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project or workspace not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity<GlobalResponse<String>> deleteProject(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId
    ) {
        projectService.deleteByIdAndWorkspaceId(projectId, workspaceId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), "Project deleted successfully"));
    }

}
