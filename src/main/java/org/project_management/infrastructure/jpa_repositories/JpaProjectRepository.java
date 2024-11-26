package org.project_management.infrastructure.jpa_repositories;


import org.project_management.domain.entities.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProjectRepository extends JpaRepository<Project, UUID> {

    @Query("SELECT p FROM Project p WHERE p.workspace.id = :workspaceId")
    List<Project> findByWorkspaceId(UUID workspaceId);

    @Query("SELECT p FROM Project p WHERE p.id = :projectId AND p.workspace.id = :workspaceId")
    Optional<Project> findByIdAndWorkspaceId(UUID projectId, UUID workspaceId);

    @Modifying
    @Query("DELETE FROM Project p WHERE p.id = :projectId AND p.workspace.id = :workspaceId")
    void deleteByIdAndWorkspaceId(UUID projectId, UUID workspaceId);
}
