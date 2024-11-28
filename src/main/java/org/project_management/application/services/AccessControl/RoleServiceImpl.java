package org.project_management.application.services.AccessControl;

import jakarta.transaction.Transactional;
import org.project_management.application.dto.role.RoleCreate;
import org.project_management.application.dto.role.RoleUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, CompanyRepository companyRepository) {
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Role save(RoleCreate roleCreateDto) {

        if (roleRepository.findByName(roleCreateDto.getName()).isPresent()) {
            throw new ResourceNotFoundException("Role with name '" + roleCreateDto.getName() + "' already exists");
        }
        Company company = companyRepository.findById(roleCreateDto.getCompanyId()).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with id: " + roleCreateDto.getCompanyId())
        );
        Role role = new Role();
        role.setCompany(company);
        role.setName(roleCreateDto.getName());

        try {
            return roleRepository.save(role);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Role with name '" + roleCreateDto.getName() + "' already exists");
        }

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
    public Role findByNameAndCompanyId(String name, UUID companyId) {
        return roleRepository.findByNameAndCompanyId(name, companyId).orElseThrow(
                () -> new ResourceNotFoundException("Role not found with name: " + name + " and company id: " + companyId)
        );
    }

    @Override
    public Role update(RoleUpdate roleUpdateDto) {
        Company company = companyRepository.findById(roleUpdateDto.getCompanyId()).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with id: " + roleUpdateDto.getCompanyId())
        );
        Role role = new Role(roleUpdateDto.getId(), roleUpdateDto.getName(), company);
        try {
            return roleRepository.update(role);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Role not found with id: " + roleUpdateDto.getId());
        }

    }

    @Override
    public void delete(UUID id) {
        try {
            roleRepository.delete(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }

    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findByCompanyId(UUID companyId) {
        return roleRepository.findByCompanyId(companyId);
    }
}
