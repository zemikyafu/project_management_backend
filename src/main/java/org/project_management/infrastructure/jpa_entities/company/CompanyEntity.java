package org.project_management.infrastructure.jpa_entities.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "company")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "Text")
    private String name;

    @Column(name="email", nullable = false, length = 320, unique = true)
    private String email;

    @Column(name="address",columnDefinition = "Text")
    private String address;

    public CompanyEntity(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }
}
