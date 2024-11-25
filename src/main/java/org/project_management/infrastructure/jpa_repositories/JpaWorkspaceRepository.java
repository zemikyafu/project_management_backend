package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaWorkspaceRepository extends JpaRepository<Workspace, UUID> {
    @Query("SELECT w FROM Workspace w WHERE w.id = :workspaceId AND w.company.id = :companyId")
    Optional<Workspace> findByIdAndCompanyId(UUID workspaceId, UUID companyId);

    @Query("SELECT w FROM Workspace w WHERE w.company.id = :companyId")
    List<Workspace> findByCompanyId(UUID companyId);

   // findAllWorkspaceUser(
    @Modifying
    @Query("DELETE FROM Workspace w WHERE w.id = :workspaceId AND w.company.id = :companyId")
    void deleteByIdAndCompanyId(UUID workspaceId, UUID companyId);
}
