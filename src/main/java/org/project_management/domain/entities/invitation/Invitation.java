package org.project_management.domain.entities.invitation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.domain.entities.role.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invitation")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @ManyToOne
    @JoinColumn(name="workspace_id", nullable = false)
    private Workspace workspace;

    @Column(name="is_accepted",nullable = false)
    private boolean isAccepted=false;

    @Column(name="expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name="token", nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;
    public Invitation(String email, Workspace workspace, Role role) {
        this.email = email;
        this.workspace = workspace;
        this.role = role;
        this.isAccepted = false;
        expiredAt = LocalDateTime.now().plusHours(24);
    }
}
