package org.project_management.application.services.User;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.CompanyUserRepository;
import org.project_management.domain.abstractions.PermissionRepository;
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

    private final PermissionRepository permissionRepository;
    public UserDetailsServiceImpl(AuthRepository authRepository, CompanyUserRepository companyUserRepository, PermissionRepository permissionRepository) {
        this.authRepository = authRepository;
        this.companyUserRepository = companyUserRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = authRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.get().getEmail())
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

        Optional<User> owner = companyUserRepository.findOwnerOfCompany(companyId);
        if (owner.isEmpty()) {
            throw new AccessDeniedException("No owner found for the company with ID: " + companyId);
        }

        if (!owner.get().getEmail().equals(email)) {
            throw new AccessDeniedException("Logged-in user is not the owner of the company with ID: " + companyId);
        }

        List<String> permissions = permissionRepository.findAll().stream().map(p -> p.getName()).collect(Collectors.toList());
        return getUserDetails(user, permissions);
    }

    private UserDetails getUserDetails(Optional<User> user, List<String> permissions) {
        List<GrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        user.get().setAuthorities(authorities);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .authorities(authorities)
                .build();
    }


}
