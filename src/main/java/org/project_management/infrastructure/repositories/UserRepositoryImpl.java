package org.project_management.infrastructure.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.project_management.infrastructure.repositories.jpa_entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity userEntity = new UserEntity(user.getName(), user.getEmail(), user.getPassword(), user.getStatus());
        entityManager.persist(userEntity);

        User newUser = new User(userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getStatus());
        newUser.setId(userEntity.getId());

        return newUser;
    }

    @Override
    public Optional<User> findById(UUID id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if (userEntity == null) {
            return Optional.empty();
        }
        User user = new User(userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getStatus());
        user.setId(userEntity.getId());

        return Optional.of(user);

    }

    @Override
    public List<User> findAll() {
        TypedQuery<UserEntity> query = entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
        List<UserEntity> userEntities = query.getResultList();

        return userEntities.stream().map((userEntity) -> {
            User user = new User(userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getStatus());
            user.setId(userEntity.getId());
            return user;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public User updateUser(User user) {
        // check if user exists
        UserEntity userEntity = entityManager.find(UserEntity.class, user.getId());
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }
        Query query = entityManager.createQuery("UPDATE UserEntity u SET u.name = :name, u.email = :email, u.password = :password, u.status = :status WHERE u.id = :id");
        query.setParameter("name", user.getName());
        query.setParameter("email", user.getEmail());
        query.setParameter("password", user.getPassword());
        query.setParameter("status", user.getStatus());
        query.setParameter("id", user.getId());
        query.executeUpdate();

        return user;
    }

    @Override
    public void deleteUser(UUID id) {

        try {
            UserEntity userEntity = entityManager.find(UserEntity.class, id);
            if (userEntity == null) {
                throw new RuntimeException("User not found");
            }
            Query query = entityManager.createQuery("DELETE FROM UserEntity u WHERE u.id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete user");
        }

    }

}
