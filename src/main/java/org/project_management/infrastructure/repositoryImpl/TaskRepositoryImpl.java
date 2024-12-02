package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.TaskRepository;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskStatus;
import org.project_management.infrastructure.jpa_repositories.JpaTaskRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final JpaTaskRepository jpaTaskRepository;

    public TaskRepositoryImpl(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }

    @Override
    public Task save(Task task) {
        try {
            jpaTaskRepository.save(task);
            return task;
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error saving task");
        }
    }

    @Override
    public List<Task> findByProjectId(UUID projectId) {
        return jpaTaskRepository.findByProjectId(projectId);
    }

    @Override
    public Optional<Task> findByIdAndProjectId(UUID taskId, UUID projectId) {
        return jpaTaskRepository.findByIdAndProjectId(taskId, projectId).or(() -> {
            throw new ResourceNotFoundException("Task not found with id: " + taskId.toString() + " and project id: " + projectId.toString());
        });
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return jpaTaskRepository.findById(taskId).or(() -> {
            throw new ResourceNotFoundException("Task not found with id: " + taskId.toString());
        });
    }

    @Override
    public Task update(Task task) {
        try {
            jpaTaskRepository.save(task);
            return task;
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error updating task");
        }
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return jpaTaskRepository.findByStatus(status);
    }

    @Override
    public List<Task> findByAssignee(UUID assigneeId) {
        return jpaTaskRepository.findByAssignee(assigneeId);
    }

    @Override
    public List<Task> findByDeadlineAtBefore(Date deadlineDate) {
     return jpaTaskRepository.findByDeadlineAtBefore(deadlineDate);
    }

    @Override
    public void deleteByIdAndProjectId(UUID taskId, UUID projectId) {
        try {
            jpaTaskRepository.deleteByIdAndProjectId(taskId, projectId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId.toString() + " and project id: " + projectId.toString());
        }
    }
}
