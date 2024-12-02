package org.project_management.application.dto.workspace;

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
public class WorkspaceUserCreate {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID roleId;

    @NotNull
    private UUID workspaceId;
}
