package org.project_management.application.services.task;

import org.project_management.domain.entities.comment.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    Comment save(Comment comment);
    Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId);
    List<Comment> findByTaskId(UUID taskId);
    Comment update(Comment comment);
    void deleteByIdAndTaskId(UUID commentId, UUID taskId);
}
