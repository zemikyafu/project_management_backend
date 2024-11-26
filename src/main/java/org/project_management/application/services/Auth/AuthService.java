package org.project_management.application.services.Auth;

import org.project_management.application.dto.user.SigninResponse;
import org.project_management.domain.entities.user.User;

public interface AuthService {
    User signUp(User user);

    SigninResponse signIn(String email, String password);
}
