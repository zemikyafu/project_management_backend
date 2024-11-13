package org.project_management.application.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserUpdate {
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
