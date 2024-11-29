package org.project_management.application.dto.invitation;

import org.project_management.domain.entities.invitation.Invitation;

public class InvitationMapper {

    private InvitationMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static InvitationResponse toInvitationResponse(Invitation invitation) {
        return new InvitationResponse(invitation.getId(), invitation.getEmail(),
                invitation.getWorkspace().getId(), invitation.getRole().getId(), invitation.isAccepted(), invitation.getExpiredAt());
    }
}
