package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.WorkspaceUserRepository;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.project_management.infrastructure.jpa_repositories.JpaWorkspaceUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkspaceUserRepositoryImpl implements WorkspaceUserRepository {
    private final JpaWorkspaceUserRepository jpaWorkspaceUserRepository;

    public WorkspaceUserRepositoryImpl(JpaWorkspaceUserRepository jpaWorkspaceUserRepository) {
        this.jpaWorkspaceUserRepository = jpaWorkspaceUserRepository;
    }

    @Override
    public WorkspaceUser save(WorkspaceUser workspaceUser) {
        try {
            return jpaWorkspaceUserRepository.save(workspaceUser);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error saving workspace user");
        }
    }

    @Override
    public List<WorkspaceUser> findByWorkspaceId(UUID workspaceId) {
        return jpaWorkspaceUserRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public Optional<WorkspaceUser> findById(UUID workspaceUserId) {
        return jpaWorkspaceUserRepository.findById(workspaceUserId).or(() -> {
            throw new UnableToSaveResourceException("Workspace user not found with id: " + workspaceUserId.toString());
        });
    }
    @Override
    public Optional<WorkspaceUser> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId) {
        return jpaWorkspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId, userId).or(() -> {
            throw new UnableToSaveResourceException("Workspace user not found with workspace id: " + workspaceId.toString() + " and user id: " + userId.toString());
        });
    }

    @Override
    public WorkspaceUser update(WorkspaceUser workspaceUser) {
        try {
            return jpaWorkspaceUserRepository.save(workspaceUser);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error updating workspace user");
        }
    }

    @Override
    public void deleteByWorkspaceIdAndUserId(UUID workspaceId, UUID userId) {
        try {
            jpaWorkspaceUserRepository.deleteByWorkspaceIdAndUserId(workspaceId, userId);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error deleting workspace user");
        }
    }
}
