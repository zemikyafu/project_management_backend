package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.User.UserCreate;
import org.project_management.application.dto.User.UserMapper;
import org.project_management.application.dto.User.UserPartialUpdate;
import org.project_management.domain.abstractions.UserRepository;
import org.project_management.domain.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void initTest() {
        List<User> users = userRepository.findAll();

        // delete all users created during tests
        for (User user : users) {
            userRepository.deleteUser(user.getId());
        }

        UserCreate userCreate = new UserCreate("Ted Tester", "testing@email.com", "Password123#");
        user = userRepository.save(UserMapper.toUser(userCreate));
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        UserCreate userCreate = new UserCreate("New User", "new@email.com", "Password123#");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreate))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(userCreate.getName()))
                .andExpect(jsonPath("$.data.email").value(userCreate.getEmail()))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.status").value(user.getStatus().toString()))
                .andExpect(jsonPath("$.data.id").value(user.getId().toString()));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").value("User deleted successfully"));

        // check that users path is empty
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldFullyUpdateExistingUser() throws Exception {
        user.setEmail("new_email@email.com");

        mockMvc.perform(put("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.email").value("new_email@email.com"))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.status").value(user.getStatus().toString()))
                .andExpect(jsonPath("$.data.id").value(user.getId().toString()));
    }

    @Test
    void shouldFailFullyUpdateNonExistentUser() throws Exception {
        mockMvc.perform(put("/api/v1/users/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("User not found"));
    }

    @Test
    void shouldPartiallyUpdateExistingUser() throws Exception {
        UserPartialUpdate userPartialUpdate = new UserPartialUpdate("New Name", "new_email@email.com");

        mockMvc.perform(patch("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPartialUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(userPartialUpdate.getName()))
                .andExpect(jsonPath("$.data.email").value(userPartialUpdate.getEmail()));
    }

    @Test
    void shouldFailPartiallyUpdateNonExistentUser() throws Exception {
        UserPartialUpdate userPartialUpdate = new UserPartialUpdate("New Name", "new_email@mail.com");

        mockMvc.perform(patch("/api/v1/users/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPartialUpdate))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("User not found"));
    }
}
