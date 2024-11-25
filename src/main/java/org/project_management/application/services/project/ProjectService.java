package org.project_management.application.services.project;

import org.project_management.application.dto.Project.ProjectCreate;
import org.project_management.application.dto.Project.ProjectUpdate;
import org.project_management.domain.entities.project.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    Project save(ProjectCreate createDTO);
    List<Project> findByWorkspaceId(UUID workspaceId);
    Optional<Project> findByIdAndWorkspaceId(UUID projectId, UUID workspaceId);
    Optional<Project> findById(UUID projectId);
    Project update(ProjectUpdate updateDTO);
    void deleteByIdAndWorkspaceId(UUID projectId, UUID workspaceId);
}
