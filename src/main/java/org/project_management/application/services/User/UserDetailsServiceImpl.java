package org.project_management.application.services.User;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.CompanyUserRepository;
import org.project_management.domain.entities.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthRepository authRepository;
    private final CompanyUserRepository companyUserRepository;

    public UserDetailsServiceImpl(AuthRepository authRepository, CompanyUserRepository companyUserRepository) {
        this.authRepository = authRepository;
        this.companyUserRepository = companyUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = authRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .build();
    }


    public UserDetails loadUserAndAuthByUsername(String email, UUID workspaceId) throws UsernameNotFoundException {
        Optional<User> user = authRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        List<String> permissions = authRepository.findGrantedAuthorities(user.get().getId(), workspaceId);

        return getUserDetails(user, permissions);
    }

    public UserDetails loadUserAsOwnerOfCompany(String email, UUID companyId) throws UsernameNotFoundException {
        Optional<User> user = authRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Validate if the user is the owner of the company
        Optional<User> owner = companyUserRepository.findOwnerOfCompany(companyId);
        if (owner.isEmpty()) {
            throw new AccessDeniedException("No owner found for the company with ID: " + companyId);
        }

        if (!owner.get().getEmail().equals(email)) {
            throw new AccessDeniedException("Logged-in user is not the owner of the company with ID: " + companyId);
        }

       //will be updated in next the PR to be fetched from DB
        List<String> permissions = List.of("MANAGE_COMPANY", "MANAGE_WORKSPACES", "MANAGE_USERS");
        return getUserDetails(user, permissions);
    }

    private UserDetails getUserDetails(Optional<User> user, List<String> permissions) {
        List<GrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        user.get().setAuthorities(authorities);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .authorities(authorities)
                .build();
    }


}
