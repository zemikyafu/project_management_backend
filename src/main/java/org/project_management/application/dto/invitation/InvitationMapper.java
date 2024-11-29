package org.project_management.application.dto.invitation;

import org.project_management.domain.entities.invitation.Invitation;

public class InvitationMapper {

    private InvitationMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static InvitationResponse toInvitationResponse(Invitation invitation) {
        InvitationResponse invitationResponse = new InvitationResponse();
        invitationResponse.setId(invitation.getId());
        invitationResponse.setRecipientEmail(invitation.getEmail());
        invitationResponse.setWorkspaceId(invitation.getWorkspace().getId());
        invitationResponse.setRoleId(invitation.getRole().getId());
        invitationResponse.setAccepted(invitation.isAccepted());
        invitationResponse.setExpiredAt(invitation.getExpiredAt());
        return invitationResponse;
    }
}
