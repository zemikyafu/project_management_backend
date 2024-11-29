package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.company.CompanyCreate;
import org.project_management.application.dto.company.CompanyMapper;
import org.project_management.application.dto.invitation.InvitationRequest;
import org.project_management.application.dto.user.SignupRequest;
import org.project_management.application.dto.workspace.WorkspaceCreate;
import org.project_management.application.dto.workspace.WorkspaceMapper;
import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.abstractions.InvitationRepository;
import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.invitation.Invitation;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@Transactional
class InvitationControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    InvitationRepository invitationRepository;

    Workspace workspace;
    Company company;
    Role role;

    @Value("${domain_url}")
    private String domainUrl;

    // TODO mock the emailService.sendEmail() method to avoid sending emails during tests

    SignupRequest userCreate = new SignupRequest(
            "Ted Tester",
            "testing@email.com",
            "Password123#"
    );

    @BeforeEach
    public void setup() throws Exception {
        // User related setup
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreate))
        );

        // Company related setup
        CompanyCreate companyCreate = new CompanyCreate("name", "email@email.com", "address");
        company = companyRepository.save(CompanyMapper.toCompany(companyCreate));

        // Workspace related setup
        WorkspaceCreate workspaceCreate = new WorkspaceCreate("new workspace 1", "description", company.getId());
        workspace = WorkspaceMapper.toEntity(workspaceCreate);
        workspace.setCompany(company);
        workspace = workspaceRepository.save(workspace);

        // Role related setup
        role = new Role("ADMIN");
        role.setCompany(company);
        role = roleRepository.save(role);
    }

    @Test
    void shouldCreateNewInvitation() throws Exception {
        InvitationRequest invitationRequest = new InvitationRequest(userCreate.getEmail(), workspace.getId(), role.getId());

        mockMvc.perform(post("/api/v1/invitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.email").value(invitationRequest.getRecipientEmail()))
                .andExpect(jsonPath("$.data.workspace").exists())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void shouldFailToCreateNewInvitationMalformedEmail() throws Exception {
        InvitationRequest invitationRequest = new InvitationRequest("not.email", workspace.getId(), role.getId());

        mockMvc.perform(post("/api/v1/invitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.errors[0].message").value("recipientEmail must be a well-formed email address"));
    }

    @Test
    void shouldAcceptExistingInvitation() throws Exception {
        InvitationRequest invitationRequest = new InvitationRequest(userCreate.getEmail(), workspace.getId(), role.getId());

        mockMvc.perform(post("/api/v1/invitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationRequest))
                );

        Invitation invitation = invitationRepository.findByEmailAndWorkspaceId(userCreate.getEmail(), workspace.getId())
                .orElseThrow(() -> new Exception("Invitation not found"));

        mockMvc.perform(get("/api/v1/invitations/accept?token=" + invitation.getToken()))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", domainUrl + "/onboarding"))
                .andExpect(redirectedUrl(domainUrl + "/onboarding"));
    }

    @Test
    void shouldNotAcceptNonExistentInvitation() throws Exception {
        mockMvc.perform(get("/api/v1/invitations/accept?token=" + UUID.randomUUID())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.errors[0].message").value("Invalid invitation token"));
    }

    // TODO
    // update invitation by id
    // get all invitations
    // Find by email and workspace ID
    // find by id
    // delete by id
}
