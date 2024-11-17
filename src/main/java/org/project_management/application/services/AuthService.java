package org.project_management.application.services;

import org.project_management.application.dto.User.SigninResponse;
import org.project_management.domain.entities.user.User;

public interface AuthService {
    public User signUp(User user);
    public SigninResponse signIn(String email, String password);
}
