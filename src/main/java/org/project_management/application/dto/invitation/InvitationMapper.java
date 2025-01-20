package org.project_management.application.dto.invitation;

import org.project_management.domain.entities.invitation.Invitation;

public class InvitationMapper {

    private InvitationMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static InvitationRead toInvitationRead(Invitation invitation) {
        InvitationRead invitationRead = new InvitationRead();
        invitationRead.setId(invitation.getId());
        invitationRead.setEmail(invitation.getEmail());
        invitationRead.setWorkspaceId(invitation.getWorkspace().getId());
        invitationRead.setWorkspaceName(invitation.getWorkspace().getName());
        invitationRead.setRoleId(invitation.getRole().getId());
        invitationRead.setRoleName(invitation.getRole().getName());
        invitationRead.setAccepted(invitation.isAccepted());
        return invitationRead;
    }
}
