package org.project_management.domain.abstractions;

import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.company.CompanyUserId;
import org.project_management.domain.entities.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyUserRepository {
    CompanyUser save(CompanyUser companyUser);
    Optional<CompanyUser> findById(CompanyUserId id);
    CompanyUser update(CompanyUser companyUser);
    void delete(UUID userId, UUID companyId);
    List<CompanyUser> findAll();
    Optional <User> findOwnerOfCompany (UUID companyId);

    List<CompanyUser> findAllByCompanyId(UUID companyId);
    Optional<Company> findOwnerCompanyByUserId(UUID userId);

}
