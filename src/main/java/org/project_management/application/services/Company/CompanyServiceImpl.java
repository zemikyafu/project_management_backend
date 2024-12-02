package org.project_management.application.services.Company;

import jakarta.transaction.Transactional;
import org.project_management.application.dto.company.CompanyUserCreate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.ResourceTakenException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.company.Company;
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

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, AuthRepository authRepository, CompanyUserService companyUserService, SecurityUtils securityUtils) {
        this.companyRepository = companyRepository;
        this.authRepository = authRepository;
        this.companyUserService = companyUserService;
        this.securityUtils = securityUtils;
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
