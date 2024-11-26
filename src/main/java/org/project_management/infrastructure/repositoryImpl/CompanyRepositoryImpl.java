package org.project_management.infrastructure.repositoryImpl;

import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.infrastructure.jpa_repositories.JpaCompanyRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
    private final JpaCompanyRepository jpaCompanyRepository;

    public CompanyRepositoryImpl(JpaCompanyRepository jpaCompanyRepository) {
        this.jpaCompanyRepository = jpaCompanyRepository;
    }

    @Override
    public Company save(Company company) {
        return jpaCompanyRepository.save(company);
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return jpaCompanyRepository.findById(id);
    }

    @Override
    public Optional<Company> findByEmail(@Param("email") String email) {
        return jpaCompanyRepository.findByEmail(email);
    }

    @Override
    public List<Company> findByEmailOrName(@Param("email") String email, @Param("name") String name) {
        return jpaCompanyRepository.findByEmailOrName(email, name);
    }

    @Override
    public Optional<Company> findByName(@Param("name") String name) {
        return jpaCompanyRepository.findByName(name);
    }

    @Override
    public Company update(Company company) {
        return jpaCompanyRepository.save(company);
    }

    @Override
    public void delete(UUID id) {
        jpaCompanyRepository.deleteById(id);
    }

    @Override
    public List<Company> findAll() {
        return jpaCompanyRepository.findAll();
    }

}
