package org.project_management.application.dto.project;

import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.workspace.Workspace;

public class ProjectMapper {

    public static Project toProject(ProjectCreate createDTO, Workspace workspace) {
        Project project = new Project();
        project.setName(createDTO.getName());
        project.setDescription(createDTO.getDescription());
        project.setStatus(createDTO.getStatus());
        project.setWorkspace(workspace);
        project.setStartDate(createDTO.getStartDate());
        project.setEndDate(createDTO.getEndDate());
        return project;
    }

    public static Project toProject(ProjectUpdate updateDTO, Project existingProject) {
        if (updateDTO.getName() != null) {
            existingProject.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            existingProject.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getStatus() != null) {
            existingProject.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getStartDate() != null) {
            existingProject.setStartDate(updateDTO.getStartDate());
        }
        if (updateDTO.getEndDate() != null) {
            existingProject.setEndDate(updateDTO.getEndDate());
        }
        return existingProject;
    }
}
