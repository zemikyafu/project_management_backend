package org.project_management.application.services.Comment;

import jakarta.servlet.http.HttpServletRequest;
import org.project_management.application.dto.comment.CommentCreate;
import org.project_management.application.dto.comment.CommentUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.CommentRepository;
import org.project_management.domain.abstractions.TaskRepository;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.comment.Comment;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.config.JwtAuthFilter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final JwtAuthFilter jwtAuthFilter;
    private final HttpServletRequest request;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository, JwtAuthFilter jwtAuthFilter, HttpServletRequest request) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.jwtAuthFilter = jwtAuthFilter;
        this.request = request;

    }

    @Override
    public Comment save(CommentCreate commentCreate) {
        String userEmail = jwtAuthFilter.getUserEmailFromToken(request);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Task task = taskRepository.findById(commentCreate.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + commentCreate.getTaskId()));

        Comment comment = new Comment(commentCreate.getContent(), task, user);
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId) {
        return commentRepository.findByIdAndTaskId(commentId, taskId);
    }

    @Override
    public List<Comment> findByTaskId(UUID taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    @Override
    public Comment update(CommentUpdate commentUpdate) {
        Comment comment = commentRepository.findById(commentUpdate.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentUpdate.getId()));
        Task task = taskRepository.findById(commentUpdate.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + commentUpdate.getTaskId()));

        comment.setContent(commentUpdate.getContent());
        comment.setTask(task);
        comment.setEditedAt(new Date());

        return commentRepository.update(comment);

    }

    @Override
    public void deleteByIdAndTaskId(UUID commentId, UUID taskId) {
        commentRepository.deleteByIdAndTaskId(commentId, taskId);
    }
}
