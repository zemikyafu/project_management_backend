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
   private String content;

    @NotNull
    private UUID taskId;

    @NotNull
    private UUID userId;
}
