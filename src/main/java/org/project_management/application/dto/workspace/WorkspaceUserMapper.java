package org.project_management.application.dto.workspace;

import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.domain.entities.workspace.WorkspaceUser;

public class WorkspaceUserMapper {
    public static WorkspaceUser toEntity(WorkspaceUserCreate createDTO, User user, Role role, Workspace workspace) {
        return new WorkspaceUser(user, role, workspace);
    }

    public static WorkspaceUser toEntity(WorkspaceUserUpdate updateDTO, WorkspaceUser existingWorkspaceUser, Role updatedRole) {
        existingWorkspaceUser.setRole(updatedRole);
        return existingWorkspaceUser;
    }
}
