package org.project_management.application.dto.Company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CompanyCreate {
    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String address;
}
