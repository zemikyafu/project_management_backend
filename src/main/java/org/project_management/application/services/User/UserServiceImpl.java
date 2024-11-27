package org.project_management.application.services.User;

import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(User user) {
      return userRepository.updateUser(user);
    }

    @Override
    public User updateNameOrEmail(User user) {
        return userRepository.updateNameOrEmail(user);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteUser(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
