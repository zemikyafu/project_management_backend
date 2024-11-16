package org.project_management.infrastructure.jpa_entities.task;

import jakarta.persistence.*;
import lombok.*;
import org.project_management.infrastructure.jpa_entities.company.CompanyEntity;
import org.project_management.infrastructure.jpa_entities.user.UserEntity;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(columnDefinition = "TEXT", name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

    @ManyToOne
    @JoinColumn(name = "commenter_id", nullable = false)
    private UserEntity user;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "edited_at")
    private Date editedAt;


    public CommentEntity(String content, TaskEntity task, UserEntity user, Date createdAt, Date editedAt) {
        this.content = content;
        this.task = task;
        this.user = user;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }

}
