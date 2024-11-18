package org.project_management.domain.entities.project;


import jakarta.persistence.*;
import lombok.*;
import org.project_management.domain.entities.workspace.Workspace;

import java.sql.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(columnDefinition = "TEXT", name = "name", nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    public Project(String name, String description, ProjectStatus status, Workspace workspace, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.workspace = workspace;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
