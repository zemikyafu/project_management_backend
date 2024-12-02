package org.project_management.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project_management.domain.entities.user.Status;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRead {
    private UUID userId;
    private String name;
    private String email;
    private Status status;
}
