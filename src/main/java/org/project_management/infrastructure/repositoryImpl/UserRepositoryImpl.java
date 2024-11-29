package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.BadRequestException;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToDeleteResourceException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.project_management.infrastructure.jpa_repositories.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id)
                .or(() -> {
                    throw new ResourceNotFoundException("User not found with id: " + id.toString());
                });
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        User updateUser = jpaUserRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getStatus() == null) {
            throw new BadRequestException("Name, email, password, and status are required");
        }

        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(user.getPassword());
        updateUser.setStatus(user.getStatus());

        try {
            return jpaUserRepository.save(updateUser);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Unable to fully update user");
        }
    }
    @Override
    public User updateNameOrEmail(User user) {
        User updateUser = jpaUserRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }

        try {
            return jpaUserRepository.save(updateUser);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Unable to partially update user");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        User user = jpaUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id.toString()));

        try {
            jpaUserRepository.delete(user);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Unable to delete user");
        }
    }

    public List<String> findGlobalAuthorities(UUID userId) {
        // Query the database to fetch global permissions for the Owner
        return List.of("MANAGE_COMPANY", "MANAGE_WORKSPACES", "MANAGE_USERS");
    }
}