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
import org.project_management.application.dto.invitation.InvitationRequest;
import org.project_management.application.dto.invitation.UpdateInvitation;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.Invitation.InvitationService;
import org.project_management.application.use_cases.InvitationUseCaseImpl;
import org.project_management.domain.entities.invitation.Invitation;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invitations")
@Tag(name = "Invitations", description = "Invitation management")
public class InvitationController {
    private final InvitationUseCaseImpl invitationUseCaseImpl;
    private final InvitationService invitationService;

    @Value("${domain_url}")
    private String domainUrl;

    public InvitationController(InvitationUseCaseImpl invitationUseCaseImpl, InvitationService invitationService) {
        this.invitationUseCaseImpl = invitationUseCaseImpl;
        this.invitationService = invitationService;
    }

    @Operation(summary = "Send an invitation email to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invitation sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email address or input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to send email")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('INVITATION-CREATE')")
    public ResponseEntity<GlobalResponse<Invitation>> invite(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details required to send an invitation",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvitationRequest.class),
                            examples = @ExampleObject(value = "{ \"recipientEmail\": \"user@example.com\", \"workspaceId\": \"5109fc9e-ce10-4bba-a9f5-f821cf66ce0b\", \"roleId\": \"5109fc9e-ce10-4bba-a9f5-f821cf66ce0b\" }")
                    )
            )
            @RequestBody @Valid InvitationRequest invitationRequestDto) {
        Invitation invitation = invitationUseCaseImpl.sendInvitation(invitationRequestDto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), invitation), HttpStatus.CREATED);
    }

    @Operation(summary = "Accept an invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect to the user onboarding page after accepting the invitation"),
            @ApiResponse(responseCode = "400", description = "Invalid token provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Invitation not found or expired"),
            @ApiResponse(responseCode = "500", description = "Internal server error while processing invitation acceptance")
    })
    @GetMapping("/accept")
    public ResponseEntity<Void> accept(
            @Parameter(description = "Token for invitation acceptance", required = true, example = "your-jwt-token")
            @RequestParam("token") String token) {
        invitationUseCaseImpl.acceptInvitation(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, domainUrl + "/onboarding")
                .build();
    }

    @Operation(summary = "Update an invitation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Invitation not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error while updating invitation")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('INVITATION-UPDATE')")
    public ResponseEntity<GlobalResponse<Invitation>> update(
            @RequestBody @Valid UpdateInvitation updateInvitationDto,
            @Parameter(description = "ID of the Invitation", required = true, example = "5109fc9e-ce10-4bba-a9f5-f821cf66ce0b")
            @PathVariable UUID id
    ) {
        Invitation invitation = invitationService.update(updateInvitationDto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitation));
    }

    @Operation(summary = "Get all invitations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "200", description = "List of all invitations retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error while retrieving invitations",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('INVITATION-READ-ALL')")
    public ResponseEntity<GlobalResponse<List<Invitation>>> findAll() {
        List<Invitation> invitations = invitationService.findAll();
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitations));
    }

    @Operation(summary = "Find an invitation by email and workspace ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "400", description = "Invalid email or workspace ID format"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Invitation not found")
    })
    @GetMapping("{email}/{workspaceId}")
    @PreAuthorize("hasAuthority('INVITATION-READ')")
    public ResponseEntity<GlobalResponse<Invitation>> findByEmailAndWorkspaceId(
            @Parameter(description = "Email address of the user", required = true, example = "user@example.com")
            @PathVariable String email,
            @Parameter(description = "Workspace ID of the invitation", required = true, example = "5109fc9e-ce10-4bba-a9f5-f821cf66ce0b")
            @PathVariable UUID workspaceId) {
        Invitation invitation = invitationService.findByEmailAndWorkspaceId(email, workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitation));
    }

    @Operation(summary = "Find Invitation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Invitation not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('INVITATION-READ')")
    public ResponseEntity<GlobalResponse<Invitation>> findById(
            @Parameter(description = "ID of the Invitation", required = true, example = "5109fc9e-ce10-4bba-a9f5-f821cf66ce0b")
            @PathVariable UUID id) {
        Invitation invitation = invitationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitation));
    }

    @Operation(summary = "Delete a resource by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Invitation not found"),
            @ApiResponse(responseCode = "400", description = "Invitation ID format"),
            @ApiResponse(responseCode = "500", description = "Internal server error while deleting the invitation")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteById(
            @Parameter(description = "ID of the resource to delete", required = true, example = "5109fc9e-ce10-4bba-a9f5-f821cf66ce0b")
            @PathVariable UUID id) {
        invitationService.delete(id);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), "Resource deleted successfully"));
    }
}
