package org.project_management.application.services.Company;

import org.project_management.domain.entities.company.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    Company save(Company company);

    Company findById(UUID companyId);

    List<Company> findAll();

    Company update(Company company);

    void delete(UUID companyId);
}
