package org.project_management.domain.entities.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.user.UserEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "company_user",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "isOwner"},name="unique_owner_per_company")})
public class CompanyUser {
    @EmbeddedId
    @Column(updatable = false)
    private CompanyUserId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @ManyToOne
    @MapsId("companyId")
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    @Column(name = "isOwner", nullable = false)
    private boolean isOwner=false;

    public CompanyUser(UserEntity user, Company company, boolean isOwner) {
        this.user = user;
        this.company = company;
        this.isOwner = isOwner;
        this.id = new CompanyUserId(user.getId(),company.getId());
    }
}