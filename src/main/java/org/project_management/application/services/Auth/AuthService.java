package org.project_management.application.services.Auth;

import org.project_management.application.dto.user.OnBoardingRequest;
import org.project_management.application.dto.user.OnBoardingResponse;
import org.project_management.application.dto.user.SigninResponse;
import org.project_management.domain.entities.user.User;

import java.util.UUID;

public interface AuthService {
    User signUp(User user);

    SigninResponse signIn(String email, String password);
    public OnBoardingResponse CompleteOnBoarding(OnBoardingRequest onBoardingRequest, UUID invitationId);
}
