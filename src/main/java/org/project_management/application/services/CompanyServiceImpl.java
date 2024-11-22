package org.project_management.application.services.company;

import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.entities.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(UUID companyId) {
        return companyRepository.findById(companyId);
    }

    @Override
    public Company update(Company company) {
        return companyRepository.update(company);
    }

    @Override
    public void delete(UUID companyId) {
        companyRepository.delete(companyId);
    }
}
