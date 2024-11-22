package org.project_management.application.dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvitationRequest {
    @Email
    @NotNull(message = "Recipient email is required")
    private String  recipientEmail;
    @NotNull(message = "Workspace ID is required")
    private UUID workspaceId;
    @NotNull(message = "Role ID is required")
    private UUID roleId;
}
