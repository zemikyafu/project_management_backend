package org.project_management.application.services.Company;

import org.project_management.application.dto.company.CompanyUserCreate;
import org.project_management.application.dto.company.CompanyUserUpdate;
import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.company.CompanyUserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyUserService {
    CompanyUser save(CompanyUserCreate companyUserCreate);
    Optional<CompanyUser> findById(CompanyUserId id);
    CompanyUser update(CompanyUserUpdate companyUserUpdate);
    void delete(UUID userId, UUID companyId);
    List<CompanyUser> findAll();
}
