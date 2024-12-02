package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.company.CompanyCreate;
import org.project_management.application.dto.company.CompanyMapper;
import org.project_management.application.dto.invitation.InvitationRequest;
import org.project_management.application.dto.invitation.UpdateInvitation;
import org.project_management.application.dto.user.SignupRequest;
import org.project_management.application.dto.workspace.WorkspaceCreate;
import org.project_management.application.dto.workspace.WorkspaceMapper;
import org.project_management.application.services.Invitation.EmailService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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

    @MockBean
    EmailService emailService;

    Workspace workspace;
    Company company;
    Role role;

    @Value("${domain_url}")
    private String domainUrl;

    SignupRequest userCreate = new SignupRequest(
            "Ted Tester",
            "testing@email.com",
            "Password123#"
    );

    @BeforeEach
    public void setup() throws Exception {
        // Mock the email service response to always return true
        when(emailService.sendEmail(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(true);

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

    public Invitation newInvitation() throws Exception {
        InvitationRequest invitationRequest = new InvitationRequest(userCreate.getEmail(), workspace.getId(), role.getId());

        mockMvc.perform(post("/api/v1/invitations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invitationRequest))
        );

        return invitationRepository
                .findByEmailAndWorkspaceId(userCreate.getEmail(), workspace.getId())
                .orElseThrow(() -> new Exception("Invitation not found"));
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
        Invitation invitation = newInvitation();
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

    @Test
    void shouldGetAllInvitations() throws Exception {
        newInvitation();

        mockMvc.perform(get("/api/v1/invitations")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].email").value(userCreate.getEmail()));
    }

    @Test
    void shouldGetInvitationById() throws Exception {
        Invitation invitation = newInvitation();

        mockMvc.perform(get("/api/v1/invitations/" + invitation.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.workspace").exists())
                .andExpect(jsonPath("$.data.accepted").value(false))
                .andExpect(jsonPath("$.data.email").value(userCreate.getEmail()));
    }

    @Test
    void shouldFailToGetNonExistentInvitationById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/invitations/" + id)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.errors[0].message").value("Invitation not found with id: " + id));
    }

    @Test
    void shouldDeleteInvitation() throws Exception {
        Invitation invitation = newInvitation();

        mockMvc.perform(delete("/api/v1/invitations/" + invitation.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").value("Resource deleted successfully"));

        // check that invitation not found
        mockMvc.perform(get("/api/v1/invitations/" + invitation.getId())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].message").value("Invitation not found with id: " + invitation.getId()))
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    void shouldUpdateExistingInvitation() throws Exception {
        Invitation invitation = newInvitation();
        UpdateInvitation updateInvitation = new UpdateInvitation();
        updateInvitation.setId(invitation.getId());
        updateInvitation.setAccepted(true);
        updateInvitation.setExpiredAt(invitation.getExpiredAt());
        updateInvitation.setRecipientEmail(userCreate.getEmail());
        updateInvitation.setRoleId(role.getId());
        updateInvitation.setWorkspaceId(workspace.getId());

        mockMvc.perform(put("/api/v1/invitations/" + invitation.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateInvitation))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.id").value(invitation.getId().toString()))
                .andExpect(jsonPath("$.data.accepted").value(true))
                .andExpect(jsonPath("$.data.email").value(userCreate.getEmail()))
                .andExpect(jsonPath("$.data.accepted").value(true));
    }

    @Test
    void shouldFindInvitationByEmailAndWorkspaceId() throws Exception {
        Invitation invitation = newInvitation();

        mockMvc.perform(get("/api/v1/invitations/" + userCreate.getEmail() + "/" + workspace.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.id").value(invitation.getId().toString()))
                .andExpect(jsonPath("$.data.email").value(userCreate.getEmail()))
                .andExpect(jsonPath("$.data.workspace").exists());
    }
}
