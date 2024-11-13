package org.project_management.application.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.project_management.domain.entities.user.Status;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRead {

    private UUID id;
    private String name;
    private String email;
    private Status Status;

}
