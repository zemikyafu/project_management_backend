package org.project_management.application.services;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> userFound = userRepository.findByEmail(email);
        if (userFound.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(userFound.get().getEmail())
                .password(userFound.get().getPassword()).build();

    }

}
