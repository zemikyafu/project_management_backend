package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaWorkspaceUserRepository extends JpaRepository<WorkspaceUser, UUID> {
    @Query("SELECT wu FROM WorkspaceUser wu WHERE wu.workspace.id = :workspaceId")
    List<WorkspaceUser> findByWorkspaceId(UUID workspaceId);

    @Query("SELECT wu FROM WorkspaceUser wu WHERE wu.workspace.id = :workspaceId AND wu.user.id = :userId")
    Optional<WorkspaceUser> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);

    @Modifying
    @Query("DELETE FROM WorkspaceUser wu WHERE wu.workspace.id = :workspaceId AND wu.user.id = :userId")
    void deleteByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);
}
