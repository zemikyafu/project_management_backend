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
public class WorkspaceUserUpdate {
    @NotNull
    private UUID id; // Primary key to identify the existing record
    @NotNull
    private UUID roleId;// Role can be updated
}
