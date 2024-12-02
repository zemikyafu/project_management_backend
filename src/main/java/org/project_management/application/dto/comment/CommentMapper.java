package org.project_management.application.dto.comment;

import org.project_management.domain.entities.comment.Comment;
import java.util.Date;

public class CommentMapper {

    private CommentMapper() {
        throw new IllegalStateException("Utility class");
    }

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
