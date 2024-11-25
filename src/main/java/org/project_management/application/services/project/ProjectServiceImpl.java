package org.project_management.application.services.project;

import org.project_management.application.dto.Project.ProjectCreate;
import org.project_management.application.dto.Project.ProjectMapper;
import org.project_management.application.dto.Project.ProjectUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.workspace.WorkspaceService;
import org.project_management.domain.abstractions.ProjectRepository;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final WorkspaceService workspaceService;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository , WorkspaceService workspaceService) {
        this.projectRepository = projectRepository;
        this.workspaceService = workspaceService;
    }

    @Override
    public Project save(ProjectCreate createDTO) {
        Workspace workspace = workspaceService.findById(createDTO.getWorkspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with ID: " + createDTO.getWorkspaceId()));
        Project project = ProjectMapper.toProject(createDTO);

        project.setWorkspace(workspace);

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
    public Optional<Project> findById(UUID projectId) {
        return projectRepository.findById(projectId);
    }

    @Override
    public Project update(ProjectUpdate updateDTO) {
        Project existingProject = projectRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Use the partial project fragment from the mapper
        Project projectFragment = ProjectMapper.toProjectFragment(updateDTO);

        // Apply changes to the existing project
        if (projectFragment.getName() != null) {
            existingProject.setName(projectFragment.getName());
        }
        if (projectFragment.getDescription() != null) {
            existingProject.setDescription(projectFragment.getDescription());
        }
        if (projectFragment.getStatus() != null) {
            existingProject.setStatus(projectFragment.getStatus());
        }
        if (projectFragment.getStartDate() != null) {
            existingProject.setStartDate(projectFragment.getStartDate());
        }
        if (projectFragment.getEndDate() != null) {
            existingProject.setEndDate(projectFragment.getEndDate());
        }

        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteByIdAndWorkspaceId(UUID projectId, UUID workspaceId) {
        projectRepository.deleteByIdAndWorkspaceId(projectId,workspaceId);
    }
}
