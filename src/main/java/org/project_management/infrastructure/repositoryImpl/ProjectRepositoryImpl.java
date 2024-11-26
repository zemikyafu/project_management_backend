package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.ProjectRepository;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.infrastructure.jpa_repositories.JpaProjectRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {
    private final JpaProjectRepository jpaProjectRepository;

    public ProjectRepositoryImpl(JpaProjectRepository jpaProjectRepository) {
        this.jpaProjectRepository = jpaProjectRepository;
    }

    @Override
    public Project save(Project project) {
        try {
            return jpaProjectRepository.save(project);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error saving project");
        }
    }

    @Override
    public List<Project> findByWorkspaceId(UUID workspaceId) {
        return jpaProjectRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public Optional<Project> findById(UUID projectId) {
        return jpaProjectRepository.findById(projectId).or(() -> {
            throw new ResourceNotFoundException("Project not found with id: " + projectId.toString());
        });
    }

    @Override
    public Optional<Project> findByIdAndWorkspaceId(UUID projectId, UUID workspaceId) {
        return jpaProjectRepository.findByIdAndWorkspaceId(projectId, workspaceId).or(() -> {
            throw new ResourceNotFoundException("Project not found with id: " + projectId.toString() + " and workspace id: " + workspaceId.toString());
        });
    }

    @Override
    public Project update(Project project) {
        try {
            return jpaProjectRepository.save(project);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error updating project");
        }
    }

    @Override
    public void deleteByIdAndWorkspaceId(UUID projectId, UUID workspaceId) {
        try {
            jpaProjectRepository.deleteByIdAndWorkspaceId(projectId, workspaceId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId.toString() + " and workspace id: " + workspaceId.toString());
        }
    }
}
