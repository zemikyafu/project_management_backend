package org.project_management.application.services.project;

import org.project_management.domain.abstractions.ProjectRepository;
import org.project_management.domain.entities.project.Project;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> findByWorkspaceId(UUID workspaceId) {
        return projectRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public Optional<Project> findByIdAndWorkspaceId(UUID projectId, UUID workspaceId) {
        return projectRepository.findByIdAndWorkspaceId(projectId,workspaceId);
    }

    @Override
    public Project update(Project project) {
        return projectRepository.update(project);
    }

    @Override
    public void deleteByIdAndWorkspaceId(UUID projectId, UUID workspaceId) {
        projectRepository.deleteByIdAndWorkspaceId(projectId,workspaceId);
    }
}
