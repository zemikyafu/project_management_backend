package org.project_management.presentation.config;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class SecurityUtils {
    private final UserRepository userJpaRepo;

    public SecurityUtils(UserRepository userJpaRepo) {
        this.userJpaRepo = userJpaRepo;
    }

    public boolean isOwner(UUID userId) {
        String authenticatedUserEmail = getCurrentUserLogin();

        String userEmail = userJpaRepo.findById(userId)
                .map(User::getEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        return authenticatedUserEmail.equals(userEmail);
    }

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails springSecurityUser) {
                userName = springSecurityUser.getUsername();
            } else if (principal instanceof String) {
                userName = principal.toString();
            }
        }

        return userName;
    }
}
