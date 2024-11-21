package org.project_management.application.services.project;

import org.project_management.domain.entities.project.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    Project save(Project project);
    List<Project> findByWorkspaceId(UUID workspaceId);
    Optional<Project> findByIdAndWorkspaceId(UUID projectId, UUID workspaceId);
    Project update(Project project);
    void deleteByIdAndWorkspaceId(UUID projectId, UUID workspaceId);
}
