package org.project_management.application.dto.project;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private UUID id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private ProjectStatus status;
    private Date startDate;
    private Date endDate;

    public void setId(UUID projectId) {
    }
}
