package org.project_management.application.services.Comment;

import org.project_management.application.dto.Comment.CommentCreate;
import org.project_management.application.dto.Comment.CommentUpdate;
import org.project_management.domain.entities.comment.Comment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface CommentService {
    Comment save(CommentCreate commentCreate);
    Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId);
    List<Comment> findByTaskId(UUID taskId);
    Comment update(CommentUpdate commentUpdate);
    void deleteByIdAndTaskId(UUID commentId, UUID taskId);
}
