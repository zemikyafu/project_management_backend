package org.project_management.application.use_cases;

import org.project_management.application.dto.invitation.InvitationRequest;
import org.project_management.domain.entities.invitation.Invitation;

public interface InvitationUseCase {
    public Invitation sendInvitation(InvitationRequest invitationRequest);

    public String acceptInvitation(String token);
}
