package org.project_management.application.services.AccessControl;

import org.project_management.application.dto.role.RolePermissionCreate;
import org.project_management.application.dto.role.RolePermissionUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.PermissionRepository;
import org.project_management.domain.abstractions.RolePermissionRepository;
import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.entities.permission.Permission;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.role.RolePermissionId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RolePermission save(RolePermissionCreate rolePermissionCreate) {
        try {
            Role role = roleRepository.findById(rolePermissionCreate.getRoleId()).orElseThrow(
                    () -> new ResourceNotFoundException("Role not found with id: " + rolePermissionCreate.getRoleId())
            );
            Permission permission = permissionRepository.findById(rolePermissionCreate.getPermissionId()).orElseThrow(
                    () -> new ResourceNotFoundException("Permission not found with id: " + rolePermissionCreate.getPermissionId())
            );
            RolePermission rolePermission = new RolePermission(role, permission);
            return rolePermissionRepository.save(rolePermission);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error creating RolePermission");
        }

    }

    @Override
    public Optional<RolePermission> findById(RolePermissionId id) {
        return rolePermissionRepository.findById(id).or(() -> {
            throw new ResourceNotFoundException("RolePermission not found with id: " + id.toString());
        });
    }

    @Override
    public RolePermission update(RolePermissionUpdate rolePermissionUpdate) {
        try {
            RolePermission rolePermission = rolePermissionRepository.findById(rolePermissionUpdate.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("RolePermission not found with id: " + rolePermissionUpdate.getId())
            );
            Role role = roleRepository.findById(rolePermissionUpdate.getRoleId()).orElseThrow(
                    () -> new ResourceNotFoundException("Role not found with id: " + rolePermissionUpdate.getRoleId())
            );
            Permission permission = permissionRepository.findById(rolePermissionUpdate.getPermissionId()).orElseThrow(
                    () -> new ResourceNotFoundException("Permission not found with id: " + rolePermissionUpdate.getPermissionId())
            );
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            return rolePermissionRepository.save(rolePermission);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error updating RolePermission");
        }

    }

    @Override
    public void delete(UUID roleId, UUID permissionId) {
        try {
            rolePermissionRepository.delete(roleId, permissionId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("RolePermission not found with roleId: " + roleId + " and permissionId: " + permissionId);
        }
    }

    @Override
    public List<RolePermission> findAll() {
        return rolePermissionRepository.findAll();
    }

    @Override
    public List<RolePermission> findAllByRoleId(UUID roleId) {
        return rolePermissionRepository.findAllByRoleId(roleId);
    }
}
