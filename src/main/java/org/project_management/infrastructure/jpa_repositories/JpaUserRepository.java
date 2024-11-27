package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserByUsername(String username);

    @Query("SELECT p.name " +
            "FROM WorkspaceUser wu " +
            "JOIN RolePermission rp ON wu.role.id = rp.role.id " +
            "JOIN Permission p ON rp.permission.id = p.id " +
            "WHERE wu.user.id = :userId AND wu.workspace.id = :workspaceId")
    List<String> findGrantedAuthorities(UUID userId, UUID workspaceId);


}
