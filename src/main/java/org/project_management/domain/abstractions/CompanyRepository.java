package org.project_management.domain.abstractions;

import org.project_management.domain.entities.company.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);

    Optional<Company> findById(UUID id);

    Optional<Company> findByEmail(String email);

    Optional<Company> findByName(String name);

    Company update(Company company);

    void delete(UUID id);

    List<Company> findAll();

    List<Company> findByEmailOrName(String email, String name);
}
