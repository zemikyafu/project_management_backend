package org.project_management.application.dto.Comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentCreate {

    @NotNull(message = "Content is required")
    private String content;

    @NotNull(message = "Task ID is required")
    private UUID taskId;

}