package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.user.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
class AuthControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    SignupRequest userCreate = new SignupRequest(
            "Ted Tester",
            "testing@email.com",
            "Password123#"
    );

    @BeforeEach
    public void addUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreate))
        );
    }

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        SignupRequest newUser = new SignupRequest("New User", "new@email.com", "Password123#");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(newUser.getName()))
                .andExpect(jsonPath("$.data.email").value(newUser.getEmail()))
                .andExpect(jsonPath("$.data.password").doesNotExist())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.userId").exists());
    }

    @Test
    void shouldFailToCreateUserIfValidationFail() throws Exception {
        SignupRequest newUser = new SignupRequest("New User", "newemail.com", "123#");

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.errors", hasSize(3)))  // Ensure there are 3 errors
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "password Password must contain at least one uppercase letter and one special character",
                        "password Password must be at least 8 characters long",
                        "email must be a well-formed email address"
                )));
    }

    @Test
    void shouldSignInExistingUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"" + userCreate.getEmail() + "\", \"password\": \"" + userCreate.getPassword() +"\" }")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(userCreate.getName()))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void shouldNotSignInWithWrongPassword() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"" + userCreate.getEmail() + "\", \"password\": \"Password1233423#\" }")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.errors[0].message").value("Unauthorized access: Bad credentials"));
    }

    @Test
    void shouldNotSignInNonExistentUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"email@email.com\", \"password\": \"Password123#\" }")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.errors[0].message").value("User not found with the provided email"));
    }
}
