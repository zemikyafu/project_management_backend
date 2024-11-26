package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.infrastructure.jpa_repositories.JpaWorkspaceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepository {
    private final JpaWorkspaceRepository jpaWorkspaceRepository;

    public WorkspaceRepositoryImpl(JpaWorkspaceRepository jpaWorkspaceRepository) {
        this.jpaWorkspaceRepository = jpaWorkspaceRepository;
    }

    @Override
    public Workspace save(Workspace workspace) {
        try {
            return jpaWorkspaceRepository.save(workspace);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error saving workspace");
        }
    }

    @Override
    public Optional<Workspace> findByIdAndCompanyId(UUID workspaceId, UUID companyId) {
        return jpaWorkspaceRepository.findByIdAndCompanyId(workspaceId, companyId).or(() -> {
            throw new ResourceNotFoundException("Workspace not found with id: " + workspaceId.toString() + " and company id: " + companyId.toString());
        });
    }

    @Override
    public Optional<Workspace> findById(UUID companyId) {
        return jpaWorkspaceRepository.findById(companyId).or(() -> {
            throw new ResourceNotFoundException("Workspace not found with id: " + companyId.toString());
        });
    }

    @Override
    public List<Workspace> findByCompanyId(UUID companyId) {
        return jpaWorkspaceRepository.findByCompanyId(companyId);
    }

    @Override
    public Workspace update(Workspace workspace) {
        try {
            return jpaWorkspaceRepository.save(workspace);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error updating workspace");
        }
    }

    @Override
    public void deleteByIdAndCompanyId(UUID workspaceId, UUID companyId) {
        try {
            jpaWorkspaceRepository.deleteByIdAndCompanyId(workspaceId, companyId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Workspace not found with id: " + workspaceId.toString() + " and company id: " + companyId.toString());
        }
    }
}
