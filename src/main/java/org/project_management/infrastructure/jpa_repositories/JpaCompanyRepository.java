package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByEmail(String email);

    Optional<Company> findByName(String name);

    @Query("SELECT c FROM Company c WHERE c.email = :email OR c.name = :name")
    List<Company> findByEmailOrName(String email, String name);
}
