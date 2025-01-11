package org.project_management.application.services.AccessControl;

import org.project_management.application.dto.role.RolePermissionCreate;
import org.project_management.application.dto.role.RolePermissionUpdate;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.role.RolePermissionId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolePermissionService {
    RolePermission save(RolePermissionCreate rolePermissionCreate);

    Optional<RolePermission> findById(RolePermissionId id);

    RolePermission update(RolePermissionUpdate rolePermissionUpdate);

    void delete(UUID roleId, UUID permissionId);

    List<RolePermission> findAll();

    List<RolePermission> findAllByRoleId(UUID roleId);
}
