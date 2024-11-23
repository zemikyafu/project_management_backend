package org.project_management.application.services;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.entities.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ResourceNotFoundException("Role with name '" + role.getName() + "' already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role findById(UUID id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role not found with id: " + id)
        );
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Role not found with name: " + name)
        );
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
