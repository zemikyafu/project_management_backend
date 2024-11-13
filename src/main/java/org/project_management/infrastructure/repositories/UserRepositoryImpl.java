package org.project_management.infrastructure.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.project_management.application.exceptions.BadRequestException;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToDeleteResourceException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.project_management.infrastructure.jpa_entities.user.UserEntity;
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

        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new BadRequestException("Name, email and password are required");
        }
        try {
            UserEntity userEntity = new UserEntity(user.getName(), user.getEmail(), user.getPassword());
            entityManager.persist(userEntity);

            User newUser = new User(userEntity.getId(),userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getStatus());
            newUser.setId(userEntity.getId());

            return newUser;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToSaveResourceException("Unable to save user");
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if (userEntity == null) {
            throw new ResourceNotFoundException("User not found with id: " + id.toString());
        }

        User user = new User(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getStatus());

        return Optional.of(user);

    }

    @Override
    public List<User> findAll() {
        TypedQuery<UserEntity> query = entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
        List<UserEntity> userEntities = query.getResultList();

        return userEntities.stream().map((userEntity) -> {
            User user = new User(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getStatus());
            return user;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            throw new BadRequestException("Email is required");
        }

        TypedQuery<UserEntity> query = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class);
        query.setParameter("email", email);
        List<UserEntity> userEntities = query.getResultList();

        if (userEntities.isEmpty()) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        UserEntity userEntity = userEntities.get(0);
        User user = new User(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getStatus());

        return Optional.of(user);
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        UserEntity userEntity = entityManager.find(UserEntity.class, user.getId());

        if (userEntity == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getStatus() == null) {
            throw new BadRequestException("Name, email, password, and status are required");
        }
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setStatus(user.getStatus());

        try {
            entityManager.merge(userEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToSaveResourceException("Unable to fully update user");
        }
        return user;
    }

     @Transactional
     public User updateNameOrEmail(User user) {
         UserEntity userEntity = entityManager.find(UserEntity.class, user.getId());

         if (userEntity == null) {
             throw new ResourceNotFoundException("User not found");
         }

         // Only update fields that are not null (for partial updates)
         if (user.getName() != null) {
             userEntity.setName(user.getName());
         }
         if (user.getEmail() != null) {
             userEntity.setEmail(user.getEmail());
         }

         try {
             entityManager.merge(userEntity);
         } catch (Exception e) {
             e.printStackTrace();
             throw new UnableToSaveResourceException("Unable to partially update user");
         }
         user.setStatus(userEntity.getStatus());
         return user;
        }

    @Transactional
    @Override
    public void deleteUser(UUID id) {

        try {
            UserEntity userEntity = entityManager.find(UserEntity.class, id);
            if (userEntity == null) {
                throw new ResourceNotFoundException("User not found with id: " + id.toString());
            }
            Query query = entityManager.createQuery("DELETE FROM UserEntity u WHERE u.id = :id");
            query.setParameter("id", id);

            query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToDeleteResourceException("Unable to delete user");
        }

    }


}