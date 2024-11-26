package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.role.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JpaRolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
}
