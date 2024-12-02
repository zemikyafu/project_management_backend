package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.role.RoleCreate;
import org.project_management.application.dto.role.RoleUpdate;
import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
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
class RoleControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    RoleRepository roleRepository;

    Company company = new Company("Company A", "testing@companyA.com", "ABC street 123");
    Role role = new Role("DEV");

    @BeforeEach
    public void addCompany() {
        company = companyRepository.save(company);
        role.setCompany(company);
        role = roleRepository.save(role);
    }

    @Test
    void createRoleSuccess() throws Exception {
        RoleCreate roleCreateDto = new RoleCreate();
        roleCreateDto.setName("PROJECT-CREATE");
        roleCreateDto.setCompanyId(company.getId());

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleCreateDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data.name").value("PROJECT-CREATE"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.company.id").value(company.getId().toString()));
    }

    @Test
    void findRoleByIdNotFound() throws Exception {
        UUID invalidId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/roles/" + invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").value((Object) null))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.errors[0].message").value("Role not found with id: " + invalidId));
    }

    @Test
    void updateRoleSuccess() throws Exception {
        RoleUpdate roleUpdate = new RoleUpdate();
        roleUpdate.setName("TASK-WRITE");
        roleUpdate.setCompanyId(company.getId());
        roleUpdate.setId(role.getId());

        mockMvc.perform(put("/api/v1/roles/" + role.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.id").value(role.getId().toString()))
                .andExpect(jsonPath("$.data.company.id").value(company.getId().toString()))
                .andExpect(jsonPath("$.data.name").value("TASK-WRITE"));
    }

    @Test
    void deleteRoleSuccess() throws Exception {
        mockMvc.perform(delete("/api/v1/roles/" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAllRolesSuccess() throws Exception {
        // Filter with company id
        mockMvc.perform(get("/api/v1/roles?companyId=" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(role.getId().toString()))
                .andExpect(jsonPath("$.data[0].name").value(role.getName()))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.errors").value((Object) null));

        // Filter with role name
        mockMvc.perform(get("/api/v1/roles?name=" + role.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(role.getId().toString()))
                .andExpect(jsonPath("$.data[0].name").value(role.getName()))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.errors").value((Object) null));

        // No filter
        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(role.getId().toString()))
                .andExpect(jsonPath("$.data[0].name").value(role.getName()))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.errors").value((Object) null));
    }
}
