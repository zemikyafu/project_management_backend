package org.project_management.application.services.Invitation;

import org.project_management.application.dto.Invitation.InvitationRequest;
import org.project_management.application.dto.Invitation.UpdateInvitation;
import org.project_management.domain.entities.invitation.Invitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationService {
    Invitation save(InvitationRequest invitationRequest);
    Optional<Invitation> findById(UUID invitationId);
    Optional<Invitation>findByEmailandWorkspaceId(String email, UUID workspaceId);
    Invitation updateInvitationStatus(String email, UUID workspaceId, boolean status);
    Invitation update(UpdateInvitation updateInvitation);

    void delete(UUID id);
    List<Invitation> findAll();
}
