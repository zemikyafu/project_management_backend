package org.project_management.application.dto.comment;

import org.project_management.domain.entities.comment.Comment;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;

import java.util.Date;

public class CommentMapper {

    public static Comment toComment(CommentCreate createDTO) {
        Comment comment = new Comment();
        comment.setContent(createDTO.getContent());
        comment.setCreatedAt(new Date());
        comment.setEditedAt(null);
        return comment;
    }

    public static Comment toCommentFragment(CommentUpdate updateDTO) {
        Comment commentFragment = new Comment();
        if (updateDTO.getContent() != null) {
            commentFragment.setContent(updateDTO.getContent());
        }
        commentFragment.setEditedAt(new Date());
        return commentFragment;
    }
}
