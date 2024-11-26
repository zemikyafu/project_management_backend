package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaCommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId AND c.task.id = :taskId")
    Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId);

    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId")
    List<Comment> findByTaskId(UUID taskId);

    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND c.task.id = :taskId")
    void deleteByIdAndTaskId(UUID commentId, UUID taskId);
}
