package org.project_management.application.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project_management.domain.entities.project.ProjectStatus;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectUpdate {
    private UUID id;
    private String name;
    private String description;
    private ProjectStatus status;
    private Date startDate;
    private Date endDate;
}
