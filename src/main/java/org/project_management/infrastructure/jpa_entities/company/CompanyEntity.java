package org.project_management.infrastructure.repositories.jpa_entities.company;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "company")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "Text")
    private String name;

    @Column(name="email", nullable = false, length = 320, unique = true)
    private String email;

    @Column(name="address",columnDefinition = "Text")
    private String address;
    public CompanyEntity() {
    }
    public CompanyEntity(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
