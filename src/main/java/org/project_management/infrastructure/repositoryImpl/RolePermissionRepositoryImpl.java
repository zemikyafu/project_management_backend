package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.RolePermissionRepository;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.role.RolePermissionId;
import org.project_management.infrastructure.jpa_repositories.JpaRolePermissionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public class RolePermissionRepositoryImpl implements RolePermissionRepository {
    private final JpaRolePermissionRepository jpaRolePermissionRepository;

    public RolePermissionRepositoryImpl(JpaRolePermissionRepository jpaRolePermissionRepository) {
        this.jpaRolePermissionRepository = jpaRolePermissionRepository;
    }

    @Override
    public RolePermission save(RolePermission rolePermission) {
        return jpaRolePermissionRepository.save(rolePermission);
    }

    @Override
    public Optional<RolePermission> findById(RolePermissionId id) {
        return jpaRolePermissionRepository.findById(id)
                .or(() -> {
                    throw new ResourceNotFoundException("RolePermission not found with id: " + id.toString());});
    }

    @Override
    public RolePermission update(RolePermission rolePermission) {
        return jpaRolePermissionRepository.save(rolePermission);
    }

    @Override
    public void delete(UUID roleId, UUID permissionId) {
        RolePermissionId id = new RolePermissionId(roleId, permissionId);
        if (jpaRolePermissionRepository.existsById(id)) {
            jpaRolePermissionRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("RolePermission not found with id: " + id.toString());
        }
    }

    @Override
    public List<RolePermission> findAll() {
        return jpaRolePermissionRepository.findAll();
    }

    @Override
    public List<RolePermission> findAllByRoleId(UUID roleId) {
        return jpaRolePermissionRepository.findAllByRoleId(roleId);
    }
}