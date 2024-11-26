package org.project_management.application.dto.project;

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
@Setter
public class ProjectUpdate {
    private UUID id; // The unique identifier of the project being updated
    private String name;
    private String description;
    private ProjectStatus status;
    private Date startDate;
    private Date endDate;

    public void setId(UUID projectId) {
    }
}
