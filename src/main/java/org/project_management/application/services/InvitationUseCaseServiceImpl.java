package org.project_management.application.services;

import lombok.Data;
import org.project_management.application.exceptions.BadRequestException;
import org.project_management.domain.use_cases.InvitationUseCase;
import org.project_management.presentation.config.JwtHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InvitationUseCaseServiceImpl implements InvitationUseCase {

    @Value("${api.base.url}")
    private String baseUrl;
    @Value("${domain_url}")
    private String domainUrl;
    private final EmailService emailService;
    private final JwtHelper jwtHelper ;

    public InvitationUseCaseServiceImpl(EmailService emailService, JwtHelper jwtHelper) {
        this.emailService = emailService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public void sendInvitation(String recipientEmail, boolean inviteByEmail) {
        if (inviteByEmail) {
            String token = jwtHelper.generateInvitationToken(recipientEmail);

            String invitationUrl = domainUrl+baseUrl+"/invitation/accept?token=" + token;
            String emailBody = "You have been invited to join the workspace. Please accept your invitation by clicking the button below:";

            emailService.sendEmail(recipientEmail, "You're Invited!", emailBody, invitationUrl, token);
        }

    }

    @Override
    public void acceptInvitation(String token) {
        if (!jwtHelper.isInvitationTokenValid(token)) {
            throw new BadRequestException("Invalid token");
        }
        else
        {
            System.out.println("Invitation accepted");
        }
    }


}
