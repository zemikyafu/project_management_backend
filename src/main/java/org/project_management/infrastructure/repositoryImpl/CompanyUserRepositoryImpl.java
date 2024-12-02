package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.UnableToDeleteResourceException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.CompanyUserRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.company.CompanyUserId;
import org.project_management.domain.entities.user.User;
import org.project_management.infrastructure.jpa_repositories.JpaCompanyUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CompanyUserRepositoryImpl implements CompanyUserRepository {
    private final JpaCompanyUserRepository jpaCompanyUserRepository;

    public CompanyUserRepositoryImpl(JpaCompanyUserRepository jpaCompanyUserRepository) {
        this.jpaCompanyUserRepository = jpaCompanyUserRepository;
    }

    @Override
    public CompanyUser save(CompanyUser companyUser) {
        try {
            return jpaCompanyUserRepository.save(companyUser);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error while saving company user");
        }
    }

    @Override
    public Optional<CompanyUser> findById(CompanyUserId id) {
        return jpaCompanyUserRepository.findById(id).or(() -> {
            throw new UnableToSaveResourceException("Company user not found");
        });
    }

    @Override
    public CompanyUser update(CompanyUser companyUser) {
        try {
            return jpaCompanyUserRepository.save(companyUser);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error while updating company user");
        }
    }

    @Override
    public void delete(UUID userId, UUID companyId) {
        try {
            jpaCompanyUserRepository.deleteByUserIdAndCompanyId(userId, companyId);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Error while deleting company user");
        }
    }

    @Override
    public List<CompanyUser> findAll() {
        return jpaCompanyUserRepository.findAll();
    }

    @Override
    public Optional<User> findOwnerOfCompany(UUID companyId) {
        return jpaCompanyUserRepository.findOwnerOfCompany(companyId, true);
    }

    @Override
    public List<CompanyUser> findAllByCompanyId(UUID companyId) {
       return jpaCompanyUserRepository.findAllByCompanyId(companyId);
    }

    @Override
    public Optional<Company> findOwnerCompanyByUserId(UUID userId) {
        return jpaCompanyUserRepository.findOwnerCompanyByUserId(userId);
    }
}
