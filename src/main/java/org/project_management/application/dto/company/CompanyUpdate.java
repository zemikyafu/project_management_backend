package org.project_management.application.dto.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompanyUpdate {
    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String address;
}
