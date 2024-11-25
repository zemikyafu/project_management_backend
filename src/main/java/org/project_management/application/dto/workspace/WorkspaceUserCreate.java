package org.project_management.application.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorkspaceUserCreate {
    private UUID userId;
    private UUID roleId;
    private UUID workspaceId;
}
