package org.project_management.domain.abstractions;

import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.role.RolePermissionId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolePermissionRepository {
    RolePermission save(RolePermission rolePermission);

    Optional<RolePermission> findById(RolePermissionId id);

    RolePermission update(RolePermission rolePermission);

    void delete(UUID roleId, UUID permissionId);

    List<RolePermission> findAll();
    List<RolePermission>findAllByRoleId(UUID roleId);
}
