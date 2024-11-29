package org.project_management.domain.abstractions;

import org.project_management.domain.entities.invitation.Invitation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository {
    Invitation save(Invitation invitation);

    Optional<Invitation> findById(UUID invitationId);

    Optional<Invitation> findByEmailandWorkspaceId(String Email, UUID workspaceId);

    Invitation update(Invitation invitation);

    void delete(UUID id);

    List<Invitation> findAll();
}
