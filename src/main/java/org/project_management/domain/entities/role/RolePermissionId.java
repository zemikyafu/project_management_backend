package org.project_management.domain.entities.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RolePermissionId implements Serializable {
    private UUID roleId;
    private UUID permissionId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolePermissionId that = (RolePermissionId) o;

        return roleId.equals(that.roleId) && permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return roleId.hashCode() + permissionId.hashCode();
    }
}
