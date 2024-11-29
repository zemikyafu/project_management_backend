package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.BadRequestException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.entities.permission.Permission;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.project_management.infrastructure.jpa_repositories.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthRepositoryImpl implements AuthRepository {
    private final JpaRoleRepository  jpaRoleRepository;
    private final JpaRolePermissionRepository jpaRolePermissionRepository;
    private final JpaWorkspaceUserRepository jpaWorkspaceUserRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaPermissionRepository jpaPermissionRepository;

    public AuthRepositoryImpl(JpaRoleRepository jpaRoleRepository, JpaRolePermissionRepository jpaRolePermissionRepository,
                              JpaWorkspaceUserRepository jpaWorkspaceUserRepository, JpaUserRepository jpaUserRepository, JpaPermissionRepository jpaPermissionRepository) {
        this.jpaRoleRepository = jpaRoleRepository;
        this.jpaRolePermissionRepository = jpaRolePermissionRepository;
        this.jpaWorkspaceUserRepository = jpaWorkspaceUserRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaPermissionRepository = jpaPermissionRepository;
    }

    @Override
    public User save(User user) {
        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new BadRequestException("Name, email and password are required");
        }
        try {
            return jpaUserRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToSaveResourceException("Unable to save user");
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public List<Role> findRoles() {
          return jpaRoleRepository.findAll();
    }

    @Override
    public List<WorkspaceUser> findWorkspaceUser() {
       return jpaWorkspaceUserRepository.findAll();
    }

    @Override
    public List<Permission> findPermissions() {
        return jpaPermissionRepository.findAll();
    }

    @Override
    public List<RolePermission> findRolePermissions() {
      return jpaRolePermissionRepository.findAll();
    }

    @Override
    @Cacheable(value = "grantedAuthoritiesCache", key = "#userId + '-' + #workspaceId")
    public List<String> findGrantedAuthorities(UUID userId, UUID workspaceId) {
        return jpaUserRepository.findGrantedAuthorities(userId, workspaceId);
    }
}
