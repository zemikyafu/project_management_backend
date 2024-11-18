package org.project_management.domain.entities.task;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import org.project_management.domain.entities.project.Project;
import org.project_management.domain.entities.user.UserEntity;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(columnDefinition = "TEXT", name = "title", nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity assignee;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "edited_at")
    private Date editedAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "closed_at")
    private Date closedAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "deadline_at")
    private Date deadlineAt;

    public Task(String title, String content, TaskPriority priority, TaskStatus status,
                Project project, UserEntity assignee, Date createdAt,
                Date editedAt, Date closedAt, Date deadlineAt) {
        this.title = title;
        this.content = content;
        this.priority = priority;
        this.status = status;
        this.project = project;
        this.assignee = assignee;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.closedAt = closedAt;
        this.deadlineAt = deadlineAt;
    }

}
