package org.project_management.application.services.Comment;

import org.project_management.application.dto.comment.CommentCreate;
import org.project_management.application.dto.comment.CommentUpdate;
import org.project_management.domain.entities.comment.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    Comment save(CommentCreate commentCreate);
    Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId);
    List<Comment> findByTaskId(UUID taskId);
    Comment update(CommentUpdate commentUpdate);
    void deleteByIdAndTaskId(UUID commentId, UUID taskId);
}
