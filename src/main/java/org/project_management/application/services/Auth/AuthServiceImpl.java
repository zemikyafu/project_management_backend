package org.project_management.application.services.Auth;

import jakarta.transaction.Transactional;
import org.project_management.application.dto.user.OnBoardingRequest;
import org.project_management.application.dto.user.OnBoardingResponse;
import org.project_management.application.dto.user.SigninResponse;
import org.project_management.application.exceptions.BadRequestException;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.application.exceptions.UserAlreadyExistException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.CompanyUserRepository;
import org.project_management.domain.abstractions.InvitationRepository;
import org.project_management.domain.abstractions.WorkspaceUserRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.invitation.Invitation;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.WorkspaceUser;
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
    private final WorkspaceUserRepository workspaceUserRepository;
    private final InvitationRepository invitationRepository;

    public AuthServiceImpl(AuthRepository authRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtHelper jwtHelper, CompanyUserRepository companyUserRepository, WorkspaceUserRepository workspaceUserRepository, InvitationRepository invitationRepository) {
        this.authRepository = authRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.companyUserRepository = companyUserRepository;
        this.workspaceUserRepository = workspaceUserRepository;
        this.invitationRepository = invitationRepository;
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

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<Company> ownerCompany = companyUserRepository.findOwnerCompanyByUserId(user.getId());
            String token;
            if (ownerCompany.isPresent()) {
                String companyId = ownerCompany.get().getId().toString();
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("companyId", companyId);
                token = jwtHelper.generateToken(extraClaims, userDetails);
            } else {
                token = jwtHelper.generateToken(userDetails);
            }

            return new SigninResponse(token, user.getId(), user.getName());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Transactional
    public OnBoardingResponse CompleteOnBoarding(OnBoardingRequest onBoardingRequest, UUID invitationId) {
        if (invitationId == null) {
            throw new BadRequestException("Invitation ID must not be null");
        }
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new ResourceNotFoundException("Invitation not found with id: " + invitationId));
        if (authRepository.findByEmail(invitation.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists with email: " + invitation.getEmail());
        }
        try {
            User user = new User(onBoardingRequest.getName(), invitation.getEmail(), passwordEncoder.encode(onBoardingRequest.getPassword()));
            authRepository.save(user);

            WorkspaceUser workspaceUser = new WorkspaceUser(user, invitation.getRole(), invitation.getWorkspace());
            workspaceUserRepository.save(workspaceUser);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(invitation.getEmail(), onBoardingRequest.getPassword()));
            String token = null;
            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("workspaceId", invitation.getWorkspace());
                token = jwtHelper.generateToken(extraClaims, userDetails);
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }

            OnBoardingResponse onBoardingResponse = new OnBoardingResponse(user.getId(), user.getName(), invitation.getEmail(), token);

            return onBoardingResponse;
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Failed to complete OnBoarding");
        }


    }
}
