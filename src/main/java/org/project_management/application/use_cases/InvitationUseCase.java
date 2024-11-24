package org.project_management.application.use_cases;

import org.project_management.application.dto.Invitation.InvitationRequest;
import org.project_management.domain.entities.invitation.Invitation;

public interface InvitationUseCase {
    public Invitation sendInvitation(InvitationRequest invitationRequest);
    public void acceptInvitation(String token);
}
