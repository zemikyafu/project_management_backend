package org.project_management.application.services.task;

import jakarta.servlet.http.HttpServletRequest;
import org.project_management.application.dto.task.TaskCreate;
import org.project_management.application.dto.task.TaskMapper;
import org.project_management.application.dto.task.TaskUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.ProjectRepository;
import org.project_management.domain.abstractions.TaskRepository;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskStatus;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.config.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final JwtAuthFilter jwtAuthFilter;
    private final HttpServletRequest request;
    private final AuthRepository authRepository;


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, JwtAuthFilter jwtAuthFilter, HttpServletRequest request, AuthRepository authRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.jwtAuthFilter = jwtAuthFilter;
        this.request = request;
        this.authRepository = authRepository;
    }


    @Override
    public Task save(TaskCreate createDto) {
        Project project = projectRepository.findById(createDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        String userEmail = jwtAuthFilter.getUserEmailFromToken(request);
        User assignee = authRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Task task = TaskMapper.toTask(createDto);
        task.setProject(project);
        task.setAssignee(assignee);

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
    public Task update(TaskUpdate taskUpdate) {
        Task task = TaskMapper.toTaskFragment(taskUpdate);
        Task existingTask = taskRepository.findById(taskUpdate.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Update the existing task with new fields
        existingTask.setTitle(task.getTitle());
        existingTask.setContent(task.getContent());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setDeadlineAt(task.getDeadlineAt());
        existingTask.setEditedAt(new java.util.Date());  // Set the edited date to current time

        return taskRepository.save(existingTask);
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
