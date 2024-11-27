package org.project_management.application.services.User;

import org.project_management.domain.entities.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findById(UUID id);

    User updateUser(User user);

    User updateNameOrEmail(User user);

    void deleteUser(UUID id);

    List<User> findAll();
}
