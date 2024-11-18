package org.project_management.domain.entities.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User {
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

    @Column(name ="created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public User() {
        this.status = Status.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = Status.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }
}
