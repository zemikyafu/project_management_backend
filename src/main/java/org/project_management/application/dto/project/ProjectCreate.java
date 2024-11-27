package org.project_management.application.dto.project;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project_management.domain.entities.project.ProjectStatus;


import java.sql.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectCreate {
    @NotNull
    private String name;
    private String description;
    @NotNull
    private ProjectStatus status;
    @NotNull
    private UUID workspaceId; // The ID of the workspace this project belongs to
    private Date startDate;
    private Date endDate;
}
