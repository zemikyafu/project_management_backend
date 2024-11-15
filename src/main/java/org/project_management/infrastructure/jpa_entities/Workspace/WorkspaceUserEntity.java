package org.project_management.infrastructure.jpa_entities.Workspace;

import jakarta.persistence.*;
import lombok.*;
import org.project_management.infrastructure.jpa_entities.project.ProjectEntity;
import org.project_management.infrastructure.jpa_entities.role.RoleEntity;
import org.project_management.infrastructure.jpa_entities.user.UserEntity;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "workspace_user")
public class WorkspaceUserEntity {
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
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;


    public WorkspaceUserEntity(UserEntity user, RoleEntity role, WorkspaceEntity workspace) {
        this.user = user;
        this.role = role;
        this.workspace = workspace;
    }
}
