package org.project_management.infrastructure.jpa_entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.infrastructure.jpa_entities.Workspace.WorkspaceEntity;
import org.project_management.infrastructure.jpa_entities.role.RoleEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invitation")
public class InvitationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
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
    public InvitationEntity(String email, WorkspaceEntity workspace, RoleEntity role) {
        this.email = email;
        this.workspace = workspace;
        this.role = role;
        this.isAccepted = false;
        expiredAt = LocalDateTime.now().plusHours(24);
    }

}
