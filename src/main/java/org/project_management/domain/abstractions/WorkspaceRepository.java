package org.project_management.domain.abstractions;

import org.project_management.domain.entities.workspace.Workspace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceRepository {
    Workspace save(Workspace workspace);
    Optional<Workspace> findByIdAndCompanyId(UUID workspaceId, UUID companyId);
    List<Workspace> findByCompanyId(UUID companyId);
    Workspace update(Workspace workspace);
    void deleteByIdAndCompanyId(UUID workspaceId, UUID companyId);

}
