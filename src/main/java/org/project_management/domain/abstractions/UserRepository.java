package org.project_management.domain.abstractions;

import org.project_management.domain.entities.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);

    User updateUser(User user);

    User updateNameOrEmail(User user);

    User updateStatus(User user);
    void deleteUser(UUID id);

    List<User> findAll();
}
