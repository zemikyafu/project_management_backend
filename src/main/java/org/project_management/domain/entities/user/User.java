package org.project_management.domain.entities.user;

import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private Status status;

    public User(String name, String email, String password, Status status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public User() {
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
