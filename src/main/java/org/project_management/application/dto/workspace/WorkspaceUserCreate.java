package org.project_management.application.dto.workspace;

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
    private UUID userId;
    private UUID roleId;
    private UUID workspaceId;

}
