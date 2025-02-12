package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.user.SignupRequest;
import org.project_management.application.dto.user.UserMapper;
import org.project_management.application.dto.user.UserPartialUpdate;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@Transactional
class UserControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthRepository authRepository;

    User user;
    SignupRequest userCreate = new SignupRequest(
            "Eddy Example",
            "testing@email.com",
            "Password123#"
    );

    @BeforeEach
    public void addUser() {
        user = authRepository.save(UserMapper.toUser(userCreate));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value(user.getName()))
                .andExpect(jsonPath("$.data[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.data[0].password").doesNotExist());
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
                .andExpect(jsonPath("$.data.userId").value(user.getId().toString()));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").value("User deleted successfully"));

        // check that user not found
        mockMvc.perform(get("/api/v1/users/" + user.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("User not found with id: " + user.getId()))
                .andExpect(jsonPath("$.data").value((Object) null));
    }

    @Test
    void shouldFullyUpdateExistingUser() throws Exception {
        user.setEmail("new_email@email.com");
        user.setName("New Name");

        mockMvc.perform(put("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value("New Name"))
                .andExpect(jsonPath("$.data.email").value("new_email@email.com"))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.status").value(user.getStatus().toString()))
                .andExpect(jsonPath("$.data.userId").value(user.getId().toString()));
    }

    @Test
    void shouldFailToUpdateNonExistentUser() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(put("/api/v1/users/" + id)
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
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/api/v1/users/" + id)
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
