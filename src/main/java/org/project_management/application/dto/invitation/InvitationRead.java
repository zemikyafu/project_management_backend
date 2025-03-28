package org.project_management.application.dto.invitation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvitationRead {
    private UUID id;
    private String  email;
    private UUID workspaceId;
    private String workspaceName;
    private UUID roleId;
    private String roleName;
    private boolean isAccepted;

}
