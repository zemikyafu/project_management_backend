package org.project_management.domain.abstractions;

import org.project_management.domain.entities.comment.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId);

    Optional<Comment> findById(UUID commentId);

    List<Comment> findByTaskId(UUID taskId);

    Comment update(Comment comment);

    void deleteByIdAndTaskId(UUID commentId, UUID taskId);
}
