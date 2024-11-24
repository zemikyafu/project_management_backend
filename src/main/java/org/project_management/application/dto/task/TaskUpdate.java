package org.project_management.application.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project_management.domain.entities.task.TaskPriority;
import org.project_management.domain.entities.task.TaskStatus;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TaskUpdate {
    @NotNull
    String title;
    @NotNull
    String content;
    @NotNull
    TaskPriority priority;
    @NotNull
    TaskStatus status;
    Date deadlineAt;
}
