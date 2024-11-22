package org.project_management.domain.use_cases;

public interface InvitationUseCase {
    public void sendInvitation( String recipientEmail, boolean inviteByEmail);
    public void acceptInvitation(String token);
}
