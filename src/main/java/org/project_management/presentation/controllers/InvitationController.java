package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.project_management.application.dto.Invitation.InvitationRequest;
import org.project_management.application.dto.Invitation.InvitationResponse;
import org.project_management.application.services.InvitationUseCaseServiceImpl;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invitation")
public class InvitationController {

    private final InvitationUseCaseServiceImpl invitationService;

    public InvitationController(InvitationUseCaseServiceImpl invitationService) {
        this.invitationService = invitationService;
    }

    @Operation(summary = "Send an invitation email to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invitation sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email address provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to send email")
    })
    @PostMapping("/send")
    public ResponseEntity<GlobalResponse<InvitationResponse>> invite(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Recipient email to send invitation", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvitationRequest.class),
                            examples = @ExampleObject(value = "{ \"recipientEmail\": \"user@example.com\"," +
                                    "\"workspaceId\": \"5109fc9e-ce10-4bba-a9f5-f821cf66ce0b\", \"roleId\": \"5109fc9e-ce10-4bba-a9f5-f821cf66ce0b\"}")
                    ))
            @RequestBody @Valid InvitationRequest invitationRequestDto) {
        invitationService.sendInvitation(invitationRequestDto.getRecipientEmail(), true);
        InvitationResponse invitationResponse = new InvitationResponse("Invitation sent successfully");
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), invitationResponse), HttpStatus.OK);
    }

    @Operation(summary = "Accept an invitation and redirect to on boarding page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect to the user on boarding page after accepting the invitation"),
            @ApiResponse(responseCode = "400", description = "Invalid token provided"),
            @ApiResponse(responseCode = "404", description = "Invitation not found or expired"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to process invitation acceptance")
    })
    @GetMapping("/accept")
    public ResponseEntity<GlobalResponse<InvitationResponse>> accept(
            @RequestParam("token") String token) {
        invitationService.acceptInvitation(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "https://to-be-implimented/user-onboarding")
                .build();
    }
}
