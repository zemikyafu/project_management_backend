package org.project_management.application.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserPartialUpdate {
    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;
}
