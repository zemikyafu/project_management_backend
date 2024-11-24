package org.project_management.application.dto.workspace;

import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.workspace.Workspace;

public class WorkspaceMapper {
    public static Workspace toEntity(WorkspaceCreate createDTO, Company company) {
        return new Workspace(
                createDTO.getName(),
                createDTO.getDescription(),
                company
        );
    }

    // as the company of a workspace can't be changed
    public static Workspace toEntity(WorkspaceUpdate updateDTO, Workspace existingWorkspace) {
        if (updateDTO.getName() != null) {
            existingWorkspace.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            existingWorkspace.setDescription(updateDTO.getDescription());
        }
        return existingWorkspace;
    }
}
