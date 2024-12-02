package org.project_management.application.dto.workspace;

import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.workspace.Workspace;

public class WorkspaceMapper {

    private WorkspaceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Workspace toEntity(WorkspaceCreate createDTO) {
        Workspace workspace = new Workspace();
        workspace.setName(createDTO.getName());
        workspace.setDescription(createDTO.getDescription());
        return workspace;
    }

    public static Workspace toEntity(WorkspaceUpdate updateDTO) {
        Workspace workspace = new Workspace();
        if (updateDTO.getName() != null) {
            workspace.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            workspace.setDescription(updateDTO.getDescription());
        }
        return workspace;
    }
}
