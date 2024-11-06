package org.project_management.application.services;

import org.project_management.domain.entities.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    void updateUser(User user);

    void deleteUser(UUID id);

    List<User> findAll();

}