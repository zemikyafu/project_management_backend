package org.project_management.application.services.task;

import org.project_management.application.dto.task.AssigneeDto;
import org.project_management.application.dto.task.TaskCreate;
import org.project_management.application.dto.task.TaskUpdate;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Task save(TaskCreate taskCreate);

    Optional<Task> findById(UUID taskId);

    List<Task> findByProjectId(UUID projectId);

    Optional<Task> findByIdAndProjectId(UUID taskId, UUID projectId);

    Task update(TaskUpdate taskUpdate);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByAssignee(UUID assigneeId);

    List<Task> findByDeadlineAtBefore(Date deadlineDate);

    void deleteByIdAndProjectId(UUID taskId, UUID projectId);
    List<AssigneeDto>findAssigneesInProjectWorkspace(UUID projectId);
}
