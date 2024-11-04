package org.project_management.infrastructure.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.project_management.infrastructure.repositories.jpa_entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(User user) {
        UserEntity userEntity = new UserEntity(user.getName(), user.getEmail(), user.getPassword(), "ACTIVE");
        entityManager.persist(userEntity);
    }

    @Override
    public Optional<User> findById(int id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if (userEntity == null) {
            return Optional.empty();
        }
        return Optional.of(new User(userEntity.getId(),
                userEntity.getName(), userEntity.getEmail(),
                userEntity.getPassword(), userEntity.getStatus()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
