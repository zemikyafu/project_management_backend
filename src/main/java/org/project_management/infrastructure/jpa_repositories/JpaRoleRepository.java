package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);

    List<Role> findByCompanyId(UUID companyId);

    Optional<Role> findByNameAndCompanyId(String name, UUID companyId);
}
