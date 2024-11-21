package org.project_management.application.services.workspace;

import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkspaceServiceImpl implements WorkspaceService{

    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Workspace save(Workspace workspace) {
        return workspaceRepository.save(workspace);
    }

    @Override
    public Optional<Workspace> findByIdAndCompanyId(UUID workspaceId, UUID companyId) {
        return workspaceRepository.findByIdAndCompanyId(workspaceId,companyId);
    }

    @Override
    public List<Workspace> findByCompanyId(UUID companyId) {
        return workspaceRepository.findByCompanyId(companyId);
    }

    @Override
    public Workspace update(Workspace workspace) {
        return workspaceRepository.update(workspace);
    }

    @Override
    public void deleteByIdAndCompanyId(UUID workspaceId, UUID companyId) {
        workspaceRepository.deleteByIdAndCompanyId(workspaceId,companyId);
    }
}
