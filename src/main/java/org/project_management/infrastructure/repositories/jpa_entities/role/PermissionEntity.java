package org.project_management.infrastructure.repositories.jpa_entities.role;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "permission")
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100,unique = true)
    private String name;

    public PermissionEntity() {
    }

    public PermissionEntity(String name) {
        this.name = name;
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
}
