package org.project_management.application.dto;

import org.project_management.domain.entities.user.Status;

import java.util.UUID;

public class UserDto {

    private UUID id;
    private String name;
    private String email;
    private Status status;

    public UserDto(UUID id, String name, String email, Status status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
