package org.project_management.infrastructure.repositories.jpa_entities.user;

import jakarta.persistence.*;
import org.project_management.infrastructure.repositories.jpa_entities.Workspace.WorkspaceEntity;
import org.project_management.infrastructure.repositories.jpa_entities.role.RoleEntity;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invitation")
public class InvitationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @ManyToOne
    @JoinColumn(name="workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @Column(name="is_accepted",nullable = false)
    private boolean isAccepted=false;

    @Column(name="expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private RoleEntity role;

    public InvitationEntity() {
        expiredAt = LocalDateTime.now().plusHours(24);
    }
    public InvitationEntity(String email, WorkspaceEntity workspace, RoleEntity role) {
        this.email = email;
        this.workspace = workspace;
        this.role = role;
        this.isAccepted = false;
        expiredAt = LocalDateTime.now().plusHours(24);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public WorkspaceEntity getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceEntity workspace) {
        this.workspace = workspace;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}
