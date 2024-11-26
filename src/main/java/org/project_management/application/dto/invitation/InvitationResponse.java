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
public class InvitationResponse {
    private UUID id;
    private String  recipientEmail;
    private UUID workspaceId;
    private UUID roleId;
    private boolean isAccepted;
    private LocalDateTime expiredAt;
}
