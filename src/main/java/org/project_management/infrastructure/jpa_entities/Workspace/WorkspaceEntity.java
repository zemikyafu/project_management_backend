package org.project_management.infrastructure.jpa_entities.Workspace;

import jakarta.persistence.*;
import org.project_management.infrastructure.jpa_entities.company.CompanyEntity;

import java.util.UUID;

@Entity
@Table(name = "workspace")
public class WorkspaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    public WorkspaceEntity() {}

    public WorkspaceEntity(String name, String description, CompanyEntity company) {
        this.name = name;
        this.description = description;
        this.company = company;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
