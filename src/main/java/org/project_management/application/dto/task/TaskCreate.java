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

    String title;
    String content;
    TaskPriority priority;
    TaskStatus status;
    UUID projectId;
    UUID assigneeId;
    Date deadlineAt;
}
