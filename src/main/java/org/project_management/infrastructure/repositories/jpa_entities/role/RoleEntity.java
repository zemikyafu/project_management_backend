package org.project_management.infrastructure.repositories.jpa_entities.user;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "role")

public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50,unique = true)
    private String name;

    public RoleEntity() {
    }

    public RoleEntity(String name) {
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
