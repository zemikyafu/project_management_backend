package org.project_management.domain.entities.workspace;

import jakarta.persistence.*;
import lombok.*;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.user.UserEntity;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "workspace_user")
public class WorkspaceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;


    public WorkspaceUser(UserEntity user, Role role, Workspace workspace) {
        this.user = user;
        this.role = role;
        this.workspace = workspace;
    }
}
