package org.project_management.infrastructure.jpa_repositories;

import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaTaskRepository extends JpaRepository<Task, UUID> {
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    List<Task> findByProjectId(UUID projectId);

    @Query("SELECT t FROM Task t WHERE t.id = :taskId AND t.project.id = :projectId")
    Optional<Task> findByIdAndProjectId(UUID taskId, UUID projectId);

    @Query("SELECT t FROM Task t WHERE t.id = :taskId")
    Optional<Task> findById(UUID taskId);

    @Query("SELECT t FROM Task t WHERE t.status = :status")
    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.assignee.id = :assigneeId")
    List<Task> findByAssignee(UUID assigneeId);

    @Query("SELECT t FROM Task t WHERE t.deadlineAt < :date")
    List<Task> findByDeadlineAtBefore(Date date);

    @Modifying
    @Query("DELETE FROM Task t WHERE t.id = :taskId AND t.project.id = :projectId")
    void deleteByIdAndProjectId(UUID taskId, UUID projectId);
}
