package org.project_management.infrastructure.repositories.jpa_entities.company;

import jakarta.persistence.*;
import org.project_management.infrastructure.repositories.jpa_entities.role.RoleEntity;
import org.project_management.infrastructure.repositories.jpa_entities.user.UserEntity;

@Entity
@Table(name = "company_user",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "isOwner"},name="unique_owner_per_company")})

public class CompanyUserEntity {
    @EmbeddedId
    private CompanyUserId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @ManyToOne
    @MapsId("companyId")
    @JoinColumn(name = "company_id",nullable = false)
    private CompanyEntity company;

    @Column(name = "isOwner", nullable = false)
    private boolean isOwner;


    public CompanyUserEntity() {
    }
    public CompanyUserEntity(UserEntity user, CompanyEntity company, boolean isOwner) {
        this.user = user;
        this.company = company;
        this.isOwner = isOwner;
        this.id = new CompanyUserId(user.getId(),company.getId());
    }

    public CompanyUserId getId() {
        return id;
    }

    public void setId(CompanyUserId id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
