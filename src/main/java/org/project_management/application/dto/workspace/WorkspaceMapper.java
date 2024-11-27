package org.project_management.application.dto.workspace;

import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.workspace.Workspace;

public class WorkspaceMapper {
    public static Workspace toEntity(WorkspaceCreate createDTO) {
        return new Workspace(
                createDTO.getName(),
                createDTO.getDescription(),
                null
        );
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
