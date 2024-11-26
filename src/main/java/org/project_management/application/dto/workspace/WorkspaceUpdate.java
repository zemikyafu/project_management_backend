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
public class WorkspaceUpdate {
    private UUID id; // ID of the workspace being updated
    private String name;
    private String description;
}
