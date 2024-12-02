package org.project_management.application.dto.user;

import org.project_management.domain.entities.user.User;

public class UserMapper {

    private UserMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static UserRead toUserRead(User user) {
        return new UserRead(user.getId(), user.getName(), user.getEmail(), user.getStatus());
    }

    public static User toUser(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
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
