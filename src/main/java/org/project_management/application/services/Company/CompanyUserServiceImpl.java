package org.project_management.application.services.Company;

import org.project_management.application.dto.company.CompanyUserCreate;
import org.project_management.application.dto.company.CompanyUserUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToUpdateResourceException;
import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.abstractions.CompanyUserRepository;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.company.CompanyUserId;
import org.project_management.domain.entities.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {
    private final CompanyUserRepository companyUserRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public CompanyUserServiceImpl(CompanyUserRepository companyUserRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.companyUserRepository = companyUserRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyUser save(CompanyUserCreate companyUserCreate) {
        User user = userRepository.findById(companyUserCreate.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + companyUserCreate.getUserId())
        );

        Company company = companyRepository.findById(companyUserCreate.getCompanyId()).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with id: " + companyUserCreate.getCompanyId())
        );

        boolean companyHasOwner = companyUserRepository.findOwnerOfCompany(companyUserCreate.getCompanyId()).isPresent();

        CompanyUser companyUser = new CompanyUser(user, company, !companyHasOwner);
        return companyUserRepository.save(companyUser);
    }

    @Override
    public Optional<CompanyUser> findById(CompanyUserId id) {
        return companyUserRepository.findById(id).or(() -> {
            throw new ResourceNotFoundException("Company user not found");
        });
    }

    @Override
    public CompanyUser update(CompanyUserUpdate companyUserUpdate) {
        try {
            CompanyUser companyUser = companyUserRepository.findById(companyUserUpdate.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Company user not found with id: " + companyUserUpdate.getId())
            );
            User user = userRepository.findById(companyUserUpdate.getUserId()).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with id: " + companyUserUpdate.getUserId())
            );

            Company company = companyRepository.findById(companyUserUpdate.getCompanyId()).orElseThrow(
                    () -> new ResourceNotFoundException("Company not found with id: " + companyUserUpdate.getCompanyId())
            );

            companyUser.setUser(user);
            companyUser.setCompany(company);
            companyUserRepository.update(companyUser);
            return companyUser;
        } catch (Exception e) {
            throw new UnableToUpdateResourceException("Error while updating company user");
        }

    }

    @Override
    public void delete(UUID userId, UUID companyId) {
        try {
            companyUserRepository.delete(userId, companyId);
        } catch (Exception e) {
            throw new UnableToUpdateResourceException("Error while deleting company user");
        }
    }

    @Override
    public List<CompanyUser> findAll() {
        return companyUserRepository.findAll();
    }
}
