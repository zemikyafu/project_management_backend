package org.project_management.application.dto.Project;

import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.workspace.Workspace;

public class ProjectMapper {

    public static Project toProject(ProjectCreate createDTO) {
        Project project = new Project();
        project.setName(createDTO.getName());
        project.setDescription(createDTO.getDescription());
        project.setStatus(createDTO.getStatus());
        project.setStartDate(createDTO.getStartDate());
        project.setEndDate(createDTO.getEndDate());
        // workspace will be null for now, and will be set in service layer
        return project;
    }

    // For updating, creates a partial Project with updated fields
    public static Project toProjectFragment(ProjectUpdate updateDTO) {
        Project projectFragment = new Project();
        projectFragment.setName(updateDTO.getName());
        projectFragment.setDescription(updateDTO.getDescription());
        projectFragment.setStatus(updateDTO.getStatus());
        projectFragment.setStartDate(updateDTO.getStartDate());
        projectFragment.setEndDate(updateDTO.getEndDate());
        // Workspace remains untouched
        return projectFragment;
    }



}
