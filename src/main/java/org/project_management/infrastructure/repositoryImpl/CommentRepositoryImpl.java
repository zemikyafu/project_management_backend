package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.CommentRepository;
import org.project_management.domain.entities.comment.Comment;
import org.project_management.infrastructure.jpa_repositories.JpaCommentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private final JpaCommentRepository jpaCommentRepository;

    public CommentRepositoryImpl(JpaCommentRepository jpaCommentRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        try {
            return jpaCommentRepository.save(comment);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error saving comment");
        }
    }

    @Override
    public Optional<Comment> findByIdAndTaskId(UUID commentId, UUID taskId) {
        return jpaCommentRepository.findByIdAndTaskId(commentId, taskId).or(() -> {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId.toString() + " and task id: " + taskId.toString());
        });
    }

    @Override
    public List<Comment> findByTaskId(UUID taskId) {
        return jpaCommentRepository.findByTaskId(taskId);
    }

    @Override
    public Optional<Comment> findById(UUID commentId) {
        return jpaCommentRepository.findById(commentId).or(() -> {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId.toString());
        });
    }

    @Override
    public Comment update(Comment comment) {
        try {
            return jpaCommentRepository.save(comment);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error updating comment");
        }

    }
    @Override
    public void deleteByIdAndTaskId(UUID commentId, UUID taskId) {
        try {
            jpaCommentRepository.deleteByIdAndTaskId(commentId, taskId);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error deleting comment");
        }
    }
}
