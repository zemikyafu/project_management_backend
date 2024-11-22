package org.project_management.application.services.role;

import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.entities.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository = null;

    /*
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
     */

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role update(Role role) {
        return roleRepository.update(role);
    }

    @Override
    public void delete(UUID id) {
        roleRepository.delete(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
