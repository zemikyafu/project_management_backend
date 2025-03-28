package org.project_management.application.services.workspace;

import org.project_management.application.dto.workspace.WorkspaceCreate;
import org.project_management.application.dto.workspace.WorkspaceUpdate;
import org.project_management.domain.entities.workspace.Workspace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceService {
    Workspace save(WorkspaceCreate createDTO);

    Optional<Workspace> findById(UUID workspaceId);

    Optional<Workspace> findByIdAndCompanyId(UUID workspaceId, UUID companyId);

    List<Workspace> findByCompanyId(UUID companyId);

    Workspace update(WorkspaceUpdate updateDTO);

    void deleteByIdAndCompanyId(UUID workspaceId, UUID companyId);

}
