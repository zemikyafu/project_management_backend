package org.project_management.application.services.task;

import org.project_management.domain.abstractions.TaskRepository;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public List<Task> findByProjectId(UUID projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public Optional<Task> findByIdAndProjectId(UUID taskId, UUID projectId) {
        return taskRepository.findByIdAndProjectId(taskId,projectId);
    }

    @Override
    public Task update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> findByAssignee(UUID assigneeId) {
        return taskRepository.findByAssignee(assigneeId);
    }

    @Override
    public List<Task> findByDeadlineAtBefore(Date deadlineDate) {
        return taskRepository.findByDeadlineAtBefore(deadlineDate);
    }

    @Override
    public void deleteByIdAndProjectId(UUID taskId, UUID projectId) {
        taskRepository.deleteByIdAndProjectId(taskId,projectId);
    }
}
