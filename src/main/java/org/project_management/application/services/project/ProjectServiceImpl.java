package org.project_management.application.services.project;

import org.project_management.application.dto.project.ProjectCreate;
import org.project_management.application.dto.project.ProjectMapper;
import org.project_management.application.dto.project.ProjectUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.ProjectRepository;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;


    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository , WorkspaceRepository workspaceRepository) {
        this.projectRepository = projectRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Project save(ProjectCreate createDTO) {
        Workspace workspace = workspaceRepository.findById(createDTO.getWorkspaceId())
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
        Project projectFragment = ProjectMapper.toProjectFragment(updateDTO);

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
