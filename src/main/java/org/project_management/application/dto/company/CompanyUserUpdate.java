package org.project_management.application.dto.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.company.CompanyUserId;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyUserUpdate {
    private CompanyUserId id;
    private UUID userId;
    private UUID companyId;
}
