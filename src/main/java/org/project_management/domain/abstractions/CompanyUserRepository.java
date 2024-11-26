package org.project_management.domain.abstractions;

import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.company.CompanyUserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyUserRepository {
    CompanyUser save(CompanyUser companyUser);
    Optional<CompanyUser> findById(CompanyUserId id);
    CompanyUser update(CompanyUser companyUser);
    void delete(UUID userId, UUID companyId);
    List<CompanyUser> findAll();
}