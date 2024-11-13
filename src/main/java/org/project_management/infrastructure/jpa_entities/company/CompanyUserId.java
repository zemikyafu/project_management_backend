package org.project_management.infrastructure.jpa_entities.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CompanyUserId  implements Serializable {
    private UUID userId;
    private UUID companyId;

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
