package org.project_management.application.dto.company;

import org.project_management.domain.entities.company.Company;

public class CompanyMapper {

    private CompanyMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Company toCompany(CompanyCreate newCompany) {
        Company company = new Company();
        company.setName(newCompany.getName());
        company.setEmail(newCompany.getEmail());
        company.setAddress(newCompany.getAddress());
        return company;
    }

    public static Company toCompany(CompanyUpdate updateCompany) {
        Company company = new Company();
        company.setName(updateCompany.getName());
        company.setEmail(updateCompany.getEmail());
        company.setAddress(updateCompany.getAddress());
        return company;
    }
}
