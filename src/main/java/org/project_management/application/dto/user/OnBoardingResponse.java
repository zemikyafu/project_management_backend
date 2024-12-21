package org.project_management.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OnBoardingResponse {

    private UUID userId;
    private String name;
    private String email;
    private String token;
}
