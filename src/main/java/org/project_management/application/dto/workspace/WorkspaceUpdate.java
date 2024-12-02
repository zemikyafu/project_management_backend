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
public class WorkspaceUpdate {
    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String description;
}
