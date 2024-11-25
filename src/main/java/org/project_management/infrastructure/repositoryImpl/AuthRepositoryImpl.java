package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.entities.permission.Permission;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.project_management.infrastructure.jpa_repositories.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public User saveUser(User user) {
       try {
           return jpaUserRepository.save(user);
       } catch (Exception e) {
           throw new UnableToSaveResourceException("Error saving user");
       }
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jpaUserRepository.findByEmail(email).or(() -> {
            throw new ResourceNotFoundException("User not found with email: " + email);
        });
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
}
