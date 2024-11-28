package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.company.CompanyUserId;
import org.project_management.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaCompanyUserRepository extends JpaRepository<CompanyUser, CompanyUserId> {
    @Modifying
    @Query("DELETE FROM CompanyUser cu WHERE cu.user.id = :userId AND cu.company.id = :companyId")
    void deleteByUserIdAndCompanyId(@Param("userId") UUID userId, @Param("companyId") UUID companyId);

    @Query("SELECT cu.user FROM CompanyUser cu WHERE cu.company.id = :companyId AND cu.isOwner = :isOwner")
    Optional<User> findOwnerOfCompany(UUID companyId, boolean isOwner);

    @Query("SELECT cu FROM CompanyUser cu WHERE cu.company.id = :companyId")
    List<CompanyUser> findAllByCompanyId(UUID companyId);

    @Query("SELECT cu.company FROM CompanyUser cu WHERE cu.user.id = :userId and cu.isOwner = true")
    Optional<Company>findOwnerCompanyByUserId(UUID userId);

}
