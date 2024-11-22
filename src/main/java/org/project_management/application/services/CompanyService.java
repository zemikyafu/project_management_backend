package org.project_management.application.services.company;

import org.project_management.domain.entities.company.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyService {
    Company save(Company project);
    Optional<Company> findById(UUID companyId);
    List<Company> findAll();
    Company update(Company company);
    void delete(UUID companyId);
}
