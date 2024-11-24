package org.project_management.application.services.task;

import org.project_management.application.dto.comment.CommentCreate;
import org.project_management.application.dto.comment.CommentMapper;
import org.project_management.application.dto.comment.CommentUpdate;
import org.project_management.application.services.User.UserService;
import org.project_management.domain.abstractions.CommentRepository;
import org.project_management.domain.entities.comment.Comment;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, TaskService taskService, UserService userService) {

        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.userService = userService;
    }


    @Override
    public Comment save(CommentCreate createDTO) {
        Task task = taskService.findById(createDTO.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        User user = userService.findById(createDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = CommentMapper.toComment(createDTO, task, user);

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

        Comment updatedComment = CommentMapper.toComment(updateDTO, existingComment);

        return commentRepository.update(updatedComment);
    }


    @Override
    public void deleteByIdAndTaskId(UUID commentId, UUID taskId) {
        commentRepository.deleteByIdAndTaskId(commentId,taskId);
    }
}
