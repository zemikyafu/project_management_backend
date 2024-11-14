package org.project_management.infrastructure.repositories.jpa_entities.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "project")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Text name;
    private Text description;
    private Date start_date;
    private Date end_date;

}
