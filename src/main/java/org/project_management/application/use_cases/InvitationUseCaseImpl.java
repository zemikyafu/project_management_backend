package org.project_management.application.use_cases;

import org.project_management.application.dto.Invitation.InvitationRequest;
import org.project_management.application.exceptions.*;
import org.project_management.application.services.Invitation.EmailService;
import org.project_management.application.services.Invitation.InvitationService;
import org.project_management.domain.entities.invitation.Invitation;
import org.project_management.presentation.config.JwtHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InvitationUseCaseImpl implements InvitationUseCase {

    @Value("${api.base.url}")
    private String baseUrl;
    @Value("${domain_url}")
    private String domainUrl;
    private final EmailService emailService;
    private final JwtHelper jwtHelper;
    private final InvitationService invitationService;

    public InvitationUseCaseImpl(EmailService emailService, JwtHelper jwtHelper, InvitationService invitationService) {
        this.emailService = emailService;
        this.jwtHelper = jwtHelper;
        this.invitationService = invitationService;
    }

    @Override
    public Invitation sendInvitation(InvitationRequest invitationRequest) {

        String token = jwtHelper.generateInvitationToken(invitationRequest.getRecipientEmail(), invitationRequest.getWorkspaceId().toString());

        String invitationUrl = domainUrl + baseUrl + "/invitation/accept?token=" + token;
        String emailBody = "You have been invited to join the workspace. Please accept your invitation by clicking the button below:";


        try {
            boolean emailSent = emailService.sendEmail(invitationRequest.getRecipientEmail(), "You're Invited!", emailBody, invitationUrl, token);
            if (emailSent) {
              return   invitationService.save(invitationRequest);
            } else {
                throw new InvitationException("Email could not be sent to the recipient.");
            }
        } catch (EmailException e) {
            throw new InvitationException("Failed to send the invitation email.");
        } catch (UnableToSaveResourceException e) {
            throw new InvitationException("Failed to save the invitation after sending the email.");
        }


    }

    @Override
    public void acceptInvitation(String token) {
        if (!jwtHelper.isInvitationTokenValid(token)) {
            throw new BadRequestException("Invalid token");
        }

        String workspaceId = jwtHelper.extractWorkspaceId(token);
        String email = jwtHelper.extractUserEmail(token);

        if (workspaceId == null || email == null) {
            throw new BadRequestException("Invalid token payload");
        }

        try {
            invitationService.updateInvitationStatus(email, UUID.fromString(workspaceId), true);
            System.out.println("Invitation accepted successfully for: " + email + " in workspace: " + workspaceId);
        } catch (ResourceNotFoundException e) {
            throw new BadRequestException("Invitation not found or already processed.");
        } catch (UnableToUpdateResourceException e) {
            throw new UnableToUpdateResourceException("Failed to update the invitation as accepted.");
        }

    }


}
