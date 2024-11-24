package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaPermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(String name);
}
