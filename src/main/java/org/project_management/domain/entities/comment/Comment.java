package org.project_management.domain.entities.comment;

import jakarta.persistence.*;
import lombok.*;
import org.project_management.domain.entities.task.Task;
import org.project_management.domain.entities.user.User;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(columnDefinition = "TEXT", name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "commenter_id", nullable = false)
    private User user;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Temporal(TemporalType.DATE)
    @Column(name = "edited_at")
    private Date editedAt;

    public Comment(String content, Task task, User user) {
        this.content = content;
        this.task = task;
        this.user = user;
        this.createdAt =  new Date();
    }
}
