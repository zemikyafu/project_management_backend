package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.role.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaRolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
 @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId")
    List<RolePermission> findAllByRoleId(UUID roleId);
}
