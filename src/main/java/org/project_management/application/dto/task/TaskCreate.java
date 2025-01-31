package org.project_management.application.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.task.TaskPriority;
import org.project_management.domain.entities.task.TaskStatus;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskCreate {
    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private TaskStatus status;

    @NotNull
    private UUID projectId;


    private UUID assigneeId;

    private Date deadlineAt;
}
