package org.project_management.application.services;

import org.project_management.application.dto.User.SigninResponse;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UserAlreadyExistException;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.config.JwtHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtHelper jwtHelper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public User signUp(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User already exists with email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public SigninResponse signIn(String email, String password) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            if(authentication.isAuthenticated()){
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtHelper.generateToken(userDetails);
                return new  SigninResponse(token, user.getId(), user.getName());
            }
            else {
                throw new BadCredentialsException("Invalid credentials");
            }
    }
}
