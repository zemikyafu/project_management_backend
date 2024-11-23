package org.project_management.application.services;

import org.project_management.domain.entities.role.Role;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    Role save(Role role);

    Role findById(UUID id);

    Role findByName(String name);

    Role update(Role role);

    void delete(UUID id);

    List<Role> findAll();
}
