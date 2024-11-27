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
import org.project_management.application.dto.task.TaskCreate;
import org.project_management.application.dto.task.TaskUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.task.TaskService;
import org.project_management.domain.entities.task.Task;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/companies/{companyId}/workspaces/{workspaceId}/projects/{projectId}/tasks")
@Tag(name = "Task", description = "Task management")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('TASK-CREATE')")
    public ResponseEntity<GlobalResponse<Task>> saveTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New task information", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskCreate.class),
                            examples = @ExampleObject(value = "{ \"title\": \"Task A\", \"content\": \"Description of Task A\", \"priority\": \"HIGH\", \"status\": \"IN_PROGRESS\", \"assigneeId\": \"123e4567-e89b-12d3-a456-426614174000\", \"deadlineAt\": \"2024-12-31\" }")
                    ))
            @Valid @RequestBody TaskCreate newTask) {
        Task savedTask = taskService.save(newTask);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), savedTask), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve all tasks in a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/")
    public ResponseEntity<GlobalResponse<List<Task>>> findAllTasksInProject(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId
    ) {
        List<Task> tasks = taskService.findByProjectId(projectId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), tasks));
    }

    @Operation(summary = "Retrieve a specific task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('TASK-READ')")
    public ResponseEntity<GlobalResponse<Task>> findTaskById(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId,
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId
    ) {
        Task task = taskService.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), task));
    }


    @Operation(summary = "Update a specific task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task or project not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{taskId}")
    @PreAuthorize("hasAuthority('TASK-UPDATE')")
    public ResponseEntity<GlobalResponse<Task>> updateTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated task data", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskUpdate.class),
                            examples = @ExampleObject(value = "{ \"title\": \"Updated Task\", \"content\": \"Updated description\", \"priority\": \"LOW\", \"status\": \"COMPLETED\", \"deadlineAt\": \"2025-06-30\" }")
                    ))
            @Valid @RequestBody TaskUpdate updateDto,
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId,
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId
    ) {
        updateDto.setId(taskId);
        Task updatedTask = taskService.update(updateDto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedTask));
    }

    @Operation(summary = "Delete a specific task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task or project not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAuthority('TASK-DELETE')")
    public ResponseEntity<GlobalResponse<String>> deleteTask(
            @Parameter(description = "Workspace ID", required = true) @PathVariable UUID workspaceId,
            @Parameter(description = "Project ID", required = true) @PathVariable UUID projectId,
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId
    ) {
        taskService.deleteByIdAndProjectId(taskId, projectId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), "Task deleted successfully"));
    }


}
