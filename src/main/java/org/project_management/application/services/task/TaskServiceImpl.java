package org.project_management.application.services.task;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.project_management.application.dto.task.AssigneeDto;
import org.project_management.application.dto.task.TaskCreate;
import org.project_management.application.dto.task.TaskMapper;
import org.project_management.application.dto.task.TaskUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.Invitation.EmailService;
import org.project_management.domain.abstractions.*;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskStatus;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.project_management.presentation.config.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final JwtAuthFilter jwtAuthFilter;
    private final HttpServletRequest request;
    private final AuthRepository authRepository;
    private final EmailService emailService;
    private final WorkspaceUserRepository workspaceUserRepository;
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, JwtAuthFilter jwtAuthFilter, HttpServletRequest request, AuthRepository authRepository, EmailService emailService, WorkspaceUserRepository workspaceUserRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.jwtAuthFilter = jwtAuthFilter;
        this.request = request;
        this.authRepository = authRepository;
        this.emailService = emailService;
        this.workspaceUserRepository = workspaceUserRepository;
    }

    @Transactional
    @Override
    public Task save(TaskCreate createDto) {
        Project project = projectRepository.findById(createDto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        String userEmail = jwtAuthFilter.getUserEmailFromToken(request);
        User assignee = authRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Task task = TaskMapper.toTask(createDto);
        task.setProject(project);
        task.setAssignee(assignee);

        String subject = "New Task Assigned: " + task.getTitle();
        String body = String.format(
                "Hello %s,\n\n" +
                        "You have been assigned a new task in the project \"%s\" with the title \"%s\".\n\n" +
                        "Please review the task and take the necessary actions.\n\n" +
                        "Best regards,\n",
                assignee.getName(),
                project.getName(),
                task.getTitle()
        );
        emailService.sendEmail(assignee.getEmail(), subject, body);
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
    @Transactional
    public void deleteByIdAndProjectId(UUID taskId, UUID projectId) {
        taskRepository.deleteByIdAndProjectId(taskId,projectId);
    }

    public List<AssigneeDto> findAssigneesInProjectWorkspace(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        Workspace workspace = project.getWorkspace();
        if (workspace == null) {
            return Collections.emptyList();
        }
        UUID workspaceId = workspace.getId();
        List<WorkspaceUser> workspaceUsers = workspaceUserRepository.findByWorkspaceId(workspaceId);

        return workspaceUsers.stream()
                .map(workspaceUser -> new AssigneeDto(
                        workspaceUser.getUser().getId(),
                        workspaceUser.getUser().getName()))
                .toList();
    }
}
