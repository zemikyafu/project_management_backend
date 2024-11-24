package org.project_management.domain.abstractions;

import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(UUID taskId);
    List<Task> findByProjectId(UUID projectId);
    Optional<Task> findByIdAndProjectId(UUID taskId, UUID projectId);
    Task update(Task task);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByAssignee(UUID assigneeId);
    List<Task> findByDeadlineAtBefore(Date deadlineDate);
    void deleteByIdAndProjectId(UUID taskId, UUID projectId);
}
