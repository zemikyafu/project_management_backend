package org.project_management.infrastructure.repositoryImpl;

import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.entities.role.Role;
import org.project_management.infrastructure.jpa_repositories.JpaRoleRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final JpaRoleRepository jpaRoleRepository;

    public RoleRepositoryImpl(JpaRoleRepository jpaRoleRepository) {
        this.jpaRoleRepository = jpaRoleRepository;
    }

    @Override
    public Role save(Role role) {
        return jpaRoleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRoleRepository.findById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRoleRepository.findByName(name);
    }

    @Override
    public Role update(Role role) {
        return jpaRoleRepository.save(role);
    }

    @Override
    public void delete(UUID id) {
        jpaRoleRepository.deleteById(id);
    }

    @Override
    public List<Role> findAll() {
        return jpaRoleRepository.findAll();
    }

    @Override
    public List<Role> findByCompanyId(@Param("company_id") UUID companyId) {
        return jpaRoleRepository.findByCompanyId(companyId);
    }

    @Override
    public Optional<Role> findByNameAndCompanyId(@Param("name") String name, @Param("company_id") UUID companyId) {
        return jpaRoleRepository.findByNameAndCompanyId(name, companyId);
    }
}
