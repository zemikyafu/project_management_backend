package org.project_management.application.services.task;

import jakarta.servlet.http.HttpServletRequest;
import org.project_management.application.dto.comment.CommentCreate;
import org.project_management.application.dto.comment.CommentMapper;
import org.project_management.application.dto.comment.CommentUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.User.UserService;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.CommentRepository;
import org.project_management.domain.abstractions.TaskRepository;
import org.project_management.domain.entities.comment.Comment;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.config.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final JwtAuthFilter jwtAuthFilter;
    private final HttpServletRequest request;
    private final AuthRepository authRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, JwtAuthFilter jwtAuthFilter, HttpServletRequest request, AuthRepository authRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.jwtAuthFilter = jwtAuthFilter;
        this.request = request;
        this.authRepository = authRepository;
    }


    @Override
    public Comment save(CommentCreate createDTO) {
        Task task = taskRepository.findById(createDTO.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + createDTO.getTaskId()));
        String userEmail = jwtAuthFilter.getUserEmailFromToken(request);
        User user = authRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment comment = CommentMapper.toComment(createDTO);
        comment.setTask(task);
        comment.setUser(user);

        return commentRepository.save(comment);
    }


    @Override
    public Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId) {
        return commentRepository.findByIdAndTaskId(commentId,taskId);
    }

    @Override
    public List<Comment> findByTaskId(UUID taskId) {
        return commentRepository.findByTaskId(taskId);
    }


    @Override
    public Comment update(CommentUpdate updateDTO) {
        Comment existingComment = commentRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        Comment commentFragment = CommentMapper.toCommentFragment(updateDTO);

        if (commentFragment.getContent() != null) {
            existingComment.setContent(commentFragment.getContent());
        }
        if (commentFragment.getEditedAt() != null) {
            existingComment.setEditedAt(commentFragment.getEditedAt());
        }

        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteByIdAndTaskId(UUID commentId, UUID taskId) {
        commentRepository.deleteByIdAndTaskId(commentId,taskId);
    }
}
