package org.project_management.application.dto.Comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentUpdate {

    @NotNull(message = "Comment ID is required")
    private UUID id;

    @NotNull(message = "Content is required")
    private String content;

    @NotNull(message = "Task ID is required")
    private UUID taskId;

}
