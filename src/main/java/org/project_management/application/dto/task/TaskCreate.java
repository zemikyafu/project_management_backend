package org.project_management.application.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project_management.domain.entities.task.TaskPriority;
import org.project_management.domain.entities.task.TaskStatus;

import java.sql.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TaskCreate {
    private String title;
    private String content;
    private TaskPriority priority;
    private TaskStatus status;
    private UUID projectId;   // The project the task belongs to
    private UUID assigneeId;  // The assignee of the task
    private Date deadlineAt;  // The task deadline
}
