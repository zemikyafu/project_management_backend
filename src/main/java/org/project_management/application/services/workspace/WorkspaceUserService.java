package org.project_management.application.services.workspace;

import org.project_management.application.dto.workspace.WorkspaceUserCreate;
import org.project_management.application.dto.workspace.WorkspaceUserUpdate;
import org.project_management.domain.entities.workspace.WorkspaceUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceUserService {
    WorkspaceUser save(WorkspaceUserCreate createDTO);

    List<WorkspaceUser> findByWorkspaceId(UUID workspaceId);

    Optional<WorkspaceUser> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);

    WorkspaceUser update(WorkspaceUserUpdate updateDTO);

    void deleteByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);
}
