package org.project_management.application.services.Auth;

import org.project_management.application.dto.user.SigninResponse;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UserAlreadyExistException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.CompanyUserRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.config.JwtHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final CompanyUserRepository companyUserRepository;
    public AuthServiceImpl(AuthRepository authRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtHelper jwtHelper, CompanyUserRepository companyUserRepository) {
        this.authRepository = authRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.companyUserRepository = companyUserRepository;
    }

    @Override
    public User signUp(User user) {
        Optional<User> existingUser = authRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User already exists with email: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authRepository.save(user);
    }

    @Override
    public SigninResponse signIn(String email, String password) {
        User user = authRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with the provided email"));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if(authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<Company> ownerCompany = companyUserRepository.findOwnerCompanyByUserId(user.getId());
            String token ;
            if(ownerCompany.isPresent())
            {
                String companyId = ownerCompany.get().getId().toString();
                Map<String, Object> extraClaims=new HashMap<>();
                extraClaims.put("companyId",companyId);
                token = jwtHelper.generateToken(extraClaims,userDetails);
            }
            else {
                token = jwtHelper.generateToken(userDetails);
            }
            return new SigninResponse(token, user.getId(), user.getName());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

}
