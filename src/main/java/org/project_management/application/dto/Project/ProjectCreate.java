package org.project_management.application.dto.Project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.project.ProjectStatus;


import java.sql.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectCreate {
    private String name;
    private String description;
    private ProjectStatus status;
    private UUID workspaceId;
    private Date startDate;
    private Date endDate;
}
