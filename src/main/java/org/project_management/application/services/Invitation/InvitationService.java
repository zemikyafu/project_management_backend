package org.project_management.application.services;

import org.project_management.application.dto.Invitation.InvitationRequest;
import org.project_management.application.dto.Invitation.UpdateInvitation;
import org.project_management.domain.entities.invitation.Invitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationService {
    Invitation save(InvitationRequest invitationRequest);
    Optional<Invitation> findById(UUID invitationId);
    Invitation update(UpdateInvitation updateInvitation);
    void delete(UUID id);
    List<Invitation> findAll();
}
