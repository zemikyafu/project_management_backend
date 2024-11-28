package org.project_management.domain.abstractions;

import org.project_management.domain.entities.permission.Permission;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.WorkspaceUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    public List<Role> findRoles();
    public List<WorkspaceUser> findWorkspaceUser();
    public List<Permission> findPermissions();
    public List<RolePermission> findRolePermissions();
    List<String> findGrantedAuthorities(UUID userId, UUID workspaceId);


}
