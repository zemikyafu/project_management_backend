package org.project_management.domain.entities.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.company.Company;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public Role(String name) {
        this.name = name;
    }
}
