package org.project_management.application.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project_management.domain.entities.task.TaskPriority;
import org.project_management.domain.entities.task.TaskStatus;

import java.util.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TaskUpdate {
    private UUID id;   // The ID of the task being updated
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private TaskPriority priority;
    @NotNull
    private TaskStatus status;
    private Date deadlineAt;
}
