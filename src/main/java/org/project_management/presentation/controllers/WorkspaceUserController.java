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
import org.project_management.application.dto.workspace.WorkspaceUserCreate;
import org.project_management.application.services.workspace.WorkspaceUserService;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/{companyId}/workspaces/{workspaceId}/users")
@Tag(name = "WorkspaceUser", description = "Manage users within a workspace")
public class WorkspaceUserController {

    private final WorkspaceUserService workspaceUserService;

    public WorkspaceUserController(WorkspaceUserService workspaceUserService) {
        this.workspaceUserService = workspaceUserService;
    }

    @Operation(summary = "Get users for a specific workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Workspace not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    @GetMapping("/")
    @PreAuthorize("hasAuthority('WORKSPACE_USER-READ')")
    public ResponseEntity<GlobalResponse<List<WorkspaceUser>>> getUsersByWorkspace(
            @Parameter(description = "company ID", required = true) @PathVariable UUID companyId,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId
    ) {
        List<WorkspaceUser> users = workspaceUserService.findByWorkspaceId(workspaceId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), users));
    }

    @Operation(summary = "Assign a user to a workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "404", description = "Workspace or user not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('WORKSPACE_USER-CREATE')")
    public ResponseEntity<GlobalResponse<WorkspaceUser>> assignUserToWorkspace(
            @Parameter(description = "company ID", required = true) @PathVariable UUID companyId,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "User ID", required = true) @PathVariable UUID userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "WorkspaceUser details to be assigned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WorkspaceUserCreate.class),
                            examples = @ExampleObject(value = "{ \"roleId\": \"123e4567-e89b-12d3-a456-426614174000\", \"workspaceId\": \"123e4567-e89b-12d3-a456-426614174001\" }")
                    ))
            @RequestBody @Valid WorkspaceUserCreate workspaceUserCreate
    ) {
        workspaceUserCreate.setWorkspaceId(workspaceId);
        workspaceUserCreate.setUserId(userId);
        WorkspaceUser assignedUser = workspaceUserService.save(workspaceUserCreate);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), assignedUser), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove a user from a workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User removed successfully"),
            @ApiResponse(responseCode = "404", description = "Workspace or user not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('WORKSPACE_USER-DELETE')")
    public ResponseEntity<GlobalResponse<String>> removeUserFromWorkspace(
            @Parameter(description = "company ID", required = true) @PathVariable UUID companyId,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "User ID", required = true) @PathVariable UUID userId
    ) {
        workspaceUserService.deleteByWorkspaceIdAndUserId(workspaceId, userId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), "User removed successfully from the workspace"));
    }
}