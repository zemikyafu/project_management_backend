package org.project_management.application.dto.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.role.RolePermissionId;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RolePermissionUpdate {
    private RolePermissionId id;
    private UUID roleId;
    private UUID permissionId;
}
