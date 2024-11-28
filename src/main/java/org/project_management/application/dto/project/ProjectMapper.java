package org.project_management.application.dto.project;

import org.project_management.domain.entities.project.Project;

public class ProjectMapper {

    public static Project toProject(ProjectCreate createDTO) {
        Project project = new Project();
        project.setName(createDTO.getName());
        project.setDescription(createDTO.getDescription());
        project.setStatus(createDTO.getStatus());
        project.setStartDate(createDTO.getStartDate());
        project.setEndDate(createDTO.getEndDate());
        return project;
    }


    public static Project toProjectFragment(ProjectUpdate updateDTO) {
        Project projectFragment = new Project();
        projectFragment.setName(updateDTO.getName());
        projectFragment.setDescription(updateDTO.getDescription());
        projectFragment.setStatus(updateDTO.getStatus());
        projectFragment.setStartDate(updateDTO.getStartDate());
        projectFragment.setEndDate(updateDTO.getEndDate());
        return projectFragment;
    }



}
