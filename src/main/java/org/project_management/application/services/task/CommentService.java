package org.project_management.application.services.task;

import org.project_management.application.dto.comment.CommentCreate;
import org.project_management.application.dto.comment.CommentUpdate;
import org.project_management.domain.entities.comment.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    Comment save(CommentCreate createDTO);
    Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId);
    List<Comment> findByTaskId(UUID taskId);
    Comment update(CommentUpdate updateDTO);
    void deleteByIdAndTaskId(UUID commentId, UUID taskId);
}
