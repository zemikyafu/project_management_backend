package org.project_management.application.services.User;

import org.project_management.application.dto.user.UpdateUserStatusRequest;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.CompanyUserRepository;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.company.CompanyUser;
import org.project_management.domain.entities.user.User;
import org.project_management.presentation.config.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyUserRepository companyUserRepository;
    private final SecurityUtils securityUtils;
    private final AuthRepository authRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CompanyUserRepository companyUserRepository, SecurityUtils securityUtils, AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyUserRepository = companyUserRepository;
        this.securityUtils = securityUtils;
        this.authRepository = authRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.updateUser(user);
    }

    @Override
    public User updateNameOrEmail(User user) {
        return userRepository.updateNameOrEmail(user);
    }

    @Override
    public User updateStatus(UpdateUserStatusRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(request.getStatus());
        return userRepository.updateStatus(user);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteUser(id);
    }

    @Override
    public List<User> findAll() {return userRepository.findAll();}

    @Override
    public List<User> findCompanyUsers(UUID id) {
        List<User> users =new ArrayList<>();
        companyUserRepository.findAllByCompanyId(id).stream().map(CompanyUser::getUser).forEach(users::add);
        return users;
    }
}
