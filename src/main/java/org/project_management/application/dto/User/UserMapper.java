package org.project_management.application.dto.User;

import org.project_management.domain.entities.user.User;

public class UserMapper {
    public static UserRead toUserRead(User user) {
        return new UserRead(user.getId(), user.getName(), user.getEmail(), user.getStatus());
    }

    public static User toUser(UserCreate userCreate) {
        User user = new User();
        user.setName(userCreate.getName());
        user.setEmail(userCreate.getEmail());
        user.setPassword(userCreate.getPassword());
        return user;
    }

    public static User toUser(UserUpdate userUpdate) {
        User user = new User();
        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());
        user.setPassword(userUpdate.getPassword());
        user.setStatus(userUpdate.getStatus());
        return user;
    }

    public static User toUser(UserPartialUpdate userUpdate) {
        User user = new User();
        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());
        return user;
    }
}
