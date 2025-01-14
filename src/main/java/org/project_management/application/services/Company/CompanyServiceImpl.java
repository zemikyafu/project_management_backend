package org.project_management.application.services.Company;

import jakarta.transaction.Transactional;
import org.project_management.application.dto.company.CompanyUserCreate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.ResourceTakenException;
import org.project_management.domain.abstractions.*;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.permission.Permission;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.role.RolePermission;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.config.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final AuthRepository authRepository;
    private final CompanyUserService companyUserService;
    private final SecurityUtils securityUtils;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, AuthRepository authRepository, CompanyUserService companyUserService, SecurityUtils securityUtils, RoleRepository roleRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.companyRepository = companyRepository;
        this.authRepository = authRepository;
        this.companyUserService = companyUserService;
        this.securityUtils = securityUtils;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Transactional
    @Override
    public Company save(Company company) {
        validateNoConflicts(company);
        Company newCompany = companyRepository.save(company);

        String email = securityUtils.getCurrentUserLogin();
        User loggedInUser = authRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        CompanyUserCreate companyUserCreate = new CompanyUserCreate(loggedInUser.getId(), newCompany.getId());
        companyUserService.save(companyUserCreate);
        Role adminRole = roleRepository.findByNameAndCompanyId("ADMIN", newCompany.getId()).orElseGet(() -> {
            Role newAdminRole = new Role("ADMIN");
            newAdminRole.setCompany(newCompany);
            roleRepository.save(newAdminRole);

            List<Permission> allPermissions = permissionRepository.findAll();
            for (Permission permission : allPermissions) {

                rolePermissionRepository.save(new RolePermission(newAdminRole, permission));
            }

            return newAdminRole;
        });

        roleRepository.findByNameAndCompanyId("GUEST", newCompany.getId()).orElseGet(() -> {
            Role newUserRole = new Role("GUEST");
            newUserRole.setCompany(newCompany);
            roleRepository.save(newUserRole);

            return newUserRole;
        });

        return newCompany;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company findById(UUID companyId) {
        return companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with id: " + companyId)
        );
    }

    @Override
    public Company update(Company company) {
        validateNoConflicts(company);
        return companyRepository.update(company);
    }

    @Override
    public void delete(UUID companyId) {
        companyRepository.delete(companyId);
    }

    private void validateNoConflicts(Company company) {
        List<Company> conflictingCompanies = companyRepository.findByEmailOrName(company.getEmail(), company.getName());

        for (Company conflictingCompany : conflictingCompanies) {
            if (!conflictingCompany.getId().equals(company.getId())) { // Ensure it's not the same company
                if (conflictingCompany.getEmail().equals(company.getEmail())) {
                    throw new ResourceTakenException("Company with email '" + company.getEmail() + "' already exists");
                }
                if (conflictingCompany.getName().equals(company.getName())) {
                    throw new ResourceTakenException("Company with name '" + company.getName() + "' already exists");
                }
            }
        }
    }
}
