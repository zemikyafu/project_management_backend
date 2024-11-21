package org.project_management.application.services.workspace;

import org.project_management.domain.abstractions.WorkspaceUserRepository;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkspaceUserServiceImpl implements WorkspaceUserService{
    private final WorkspaceUserRepository workspaceUserRepository;

    @Autowired
    public WorkspaceUserServiceImpl(WorkspaceUserRepository workspaceUserRepository) {
        this.workspaceUserRepository = workspaceUserRepository;
    }

    @Override
    public WorkspaceUser save(WorkspaceUser workspaceUser) {
        return workspaceUserRepository.save(workspaceUser);
    }

    @Override
    public List<WorkspaceUser> findByWorkspaceId(UUID workspaceId) {
        return workspaceUserRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public Optional<WorkspaceUser> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId) {
        return workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId,userId);
    }

    @Override
    public WorkspaceUser update(WorkspaceUser workspaceUser) {
        return workspaceUserRepository.update(workspaceUser);
    }

    @Override
    public void deleteByWorkspaceIdAndUserId(UUID workspaceId, UUID userId) {
        workspaceUserRepository.deleteByWorkspaceIdAndUserId(workspaceId,userId);
    }
}
