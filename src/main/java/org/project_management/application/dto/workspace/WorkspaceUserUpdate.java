package org.project_management.application.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorkspaceUserUpdate {
    private UUID id; // Primary key to identify the existing record
    private UUID roleId; // Role can be updated
}
