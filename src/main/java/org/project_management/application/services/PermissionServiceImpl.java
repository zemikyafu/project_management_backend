package org.project_management.application.services;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.PermissionRepository;
import org.project_management.domain.entities.permission.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission findById(UUID id) {
        return permissionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Permission not found with id: " + id)
        );
    }

    @Override
    public Permission findByName(String name) {
        return permissionRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Permission not found with name: " + name)
        );
    }

    @Override
    public Permission update(Permission permission) {
        return permissionRepository.update(permission);
    }

    @Override
    public void delete(UUID id) {
        permissionRepository.delete(id);
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }
}
