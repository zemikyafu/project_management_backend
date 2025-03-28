package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.project_management.application.dto.comment.CommentCreate;
import org.project_management.application.dto.comment.CommentUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.Comment.CommentService;
import org.project_management.domain.entities.comment.Comment;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@Tag(name = "Comment", description = "Comment management")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Create a new comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "403", description = "Forbidden access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('COMMENT-CREATE')")
    public ResponseEntity<GlobalResponse<Comment>> save(
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details for creating a comment",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentCreate.class))
            )
            @RequestBody @Valid CommentCreate commentCreateDto) {
        Comment comment = commentService.save(commentCreateDto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), comment), HttpStatus.CREATED);
    }

    @Operation(summary = "Find a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment or task ID"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{commentId}")
    @PreAuthorize("hasAuthority('COMMENT-READ')")
    public ResponseEntity<GlobalResponse<Comment>> findById(
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId,
            @Parameter(description = "Comment ID", required = true) @PathVariable UUID commentId) {
        Comment comment = commentService.findByIdAndTaskId(commentId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), comment));
    }

    @Operation(summary = "Find all comments for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid task ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('COMMENT-READ-ALL')")
    public ResponseEntity<GlobalResponse<List<Comment>>> findByTaskId(
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId) {
        List<Comment> comments = commentService.findByTaskId(taskId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), comments));
    }

    @Operation(summary = "Update an existing comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{commentId}")
    @PreAuthorize("hasAuthority('COMMENT-UPDATE')")
    public ResponseEntity<GlobalResponse<Comment>> update(
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId,
            @Parameter(description = "Comment ID", required = true) @PathVariable UUID commentId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details for updating a comment",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentUpdate.class))
            )
            @RequestBody @Valid CommentUpdate commentUpdateDto) {
        Comment updatedComment = commentService.update(commentUpdateDto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedComment));
    }

    @Operation(summary = "Delete a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid IDs"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAuthority('COMMENT-DELETE')")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Task ID", required = true) @PathVariable UUID taskId,
            @Parameter(description = "Comment ID", required = true) @PathVariable UUID commentId) {
        commentService.deleteByIdAndTaskId(commentId, taskId);
        return ResponseEntity.noContent().build();
    }
}
