package org.project_management.application.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentCreate {
    @NotNull
    String content;

    @NotNull
    UUID taskId;

    @NotNull
    UUID userId;
}