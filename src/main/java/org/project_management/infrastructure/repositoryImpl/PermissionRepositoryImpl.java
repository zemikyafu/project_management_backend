package org.project_management.infrastructure.repositoryImpl;

import org.project_management.domain.abstractions.PermissionRepository;
import org.project_management.domain.entities.permission.Permission;
import org.project_management.infrastructure.jpa_repositories.JpaPermissionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PermissionRepositoryImpl implements PermissionRepository {
    private final JpaPermissionRepository jpaPermissionRepository;

    public PermissionRepositoryImpl(JpaPermissionRepository jpaPermissionRepository) {
        this.jpaPermissionRepository = jpaPermissionRepository;
    }

    @Override
    public Permission save(Permission permission) {
        return jpaPermissionRepository.save(permission);
    }

    @Override
    public Optional<Permission> findById(UUID id) {
        return jpaPermissionRepository.findById(id);
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return jpaPermissionRepository.findByName(name);
    }

    @Override
    public Permission update(Permission permission) {
        return jpaPermissionRepository.save(permission);
    }

    @Override
    public void delete(UUID id) {
        jpaPermissionRepository.deleteById(id);
    }

    @Override
    public List<Permission> findAll() {
        return jpaPermissionRepository.findAll();
    }
}
