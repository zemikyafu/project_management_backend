package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.invitation.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaInvitationRepository extends JpaRepository<Invitation, UUID> {
    Optional<Invitation> findByEmailAndWorkspaceId(String email, UUID workspaceId);
}
