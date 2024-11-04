package org.project_management.domain.abstractions;

import org.project_management.domain.entities.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    void updateUser(User user);

    void deleteUser(int id);

    List<User> findAll();
}
