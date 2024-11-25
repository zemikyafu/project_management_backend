package org.project_management.application.dto.comment;

import org.project_management.domain.entities.comment.Comment;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;

public class CommentMapper {
    public static Comment toComment(CommentCreate createDTO, Task task, User user) {
        return new Comment(
                createDTO.getContent(),
                task,
                user,
                new java.util.Date(),
                null
        );
    }

    public static Comment toComment(CommentUpdate updateDTO, Comment existingComment) {
        if (updateDTO.getContent() != null) {
            existingComment.setContent(updateDTO.getContent());
            existingComment.setEditedAt(new java.util.Date());
        }
        return existingComment;
    }
}
