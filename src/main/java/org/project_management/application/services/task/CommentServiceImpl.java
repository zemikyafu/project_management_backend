package org.project_management.application.services.task;

import org.project_management.domain.abstractions.CommentRepository;
import org.project_management.domain.entities.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
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
    public Comment update(Comment comment) {
        return commentRepository.update(comment);
    }

    @Override
    public void deleteByIdAndTaskId(UUID commentId, UUID taskId) {
        commentRepository.deleteByIdAndTaskId(commentId,taskId);
    }
}
