package org.project_management.application.dto.workspace;

import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.domain.entities.workspace.WorkspaceUser;

public class WorkspaceUserMapper {

    public static WorkspaceUser toEntity(WorkspaceUserCreate createDTO) {
        return new WorkspaceUser();
    }

    public static WorkspaceUser toEntity(WorkspaceUserUpdate updateDTO) {
        WorkspaceUser workspaceUser = new WorkspaceUser();
        return new WorkspaceUser();
    }
}
