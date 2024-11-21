package org.project_management.domain.abstractions;

import org.project_management.domain.entities.workspace.WorkspaceUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceUserRepository {
    WorkspaceUser save(WorkspaceUser workspaceUser);
    List<WorkspaceUser> findByWorkspaceId(UUID workspaceId);
    Optional<WorkspaceUser> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);
    WorkspaceUser update(WorkspaceUser workspaceUser);
    void deleteByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);
}
