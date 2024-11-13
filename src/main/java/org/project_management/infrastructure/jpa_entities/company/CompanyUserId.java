package org.project_management.infrastructure.repositories.jpa_entities.company;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CompanyUserId  implements Serializable {
    private UUID userId;
    private UUID companyId;

    public CompanyUserId() {
    }

    public CompanyUserId(UUID userId, UUID companyId) {
        this.userId = userId;
        this.companyId = companyId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyUserId that = (CompanyUserId) o;

        return Objects.equals(userId, that.userId) && Objects.equals(companyId, that.companyId);

    }
    @Override
    public int hashCode() {
        return Objects.hash(userId, companyId);
    }
}
