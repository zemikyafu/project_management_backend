package org.project_management.application.services.Invitation;

import org.project_management.application.dto.invitation.InvitationRequest;
import org.project_management.application.dto.invitation.UpdateInvitation;
import org.project_management.domain.entities.invitation.Invitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationService {
    Invitation save(InvitationRequest invitationRequest);

    Optional<Invitation> findById(UUID invitationId);

    Optional<Invitation>findByEmailAndWorkspaceId(String email, UUID workspaceId);

    Invitation updateInvitationStatus(String email, UUID workspaceId, boolean status);

    Invitation update(UpdateInvitation updateInvitation);

    void delete(UUID id);

    List<Invitation> findAll();
}
