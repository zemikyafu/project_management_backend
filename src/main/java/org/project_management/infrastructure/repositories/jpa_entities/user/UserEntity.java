package org.project_management.infrastructure.repositories.jpa_entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.project_management.domain.entities.user.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 320)
    private String email;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private Status status;

    @Column(name ="created_at", nullable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public UserEntity() {
    }

    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = Status.ACTIVE;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
