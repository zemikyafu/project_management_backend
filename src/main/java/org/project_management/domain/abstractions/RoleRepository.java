package org.project_management.domain.abstractions;

import org.project_management.domain.entities.role.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    Optional<Role> findByName(String name);
    Role update(Role role);
    void delete(UUID id);
    List<Role> findAll();
    List<Role> findByCompanyId(UUID companyId);
}
