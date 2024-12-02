package org.project_management.application.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleUpdate {
    @NotNull(message = "ID is required")
    private UUID id;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Company ID is required")
    private UUID companyId;
}
