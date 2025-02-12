package org.project_management.domain.abstractions;

import org.project_management.domain.entities.permission.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Permission save(Permission permission);

    Optional<Permission> findById(UUID id);

    Optional<Permission> findByName(String name);

    Permission update(Permission permission);

    void delete(UUID id);

    List<Permission> findAll();
}
