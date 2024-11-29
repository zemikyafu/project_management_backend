package org.project_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.project_management.application.dto.company.CompanyCreate;
import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.entities.company.Company;
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
class CompanyControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CompanyRepository companyRepository;

    Company company = new Company("Company A", "testing@companyA.com", "ABC street 123");

    @BeforeEach
    public void addCompany() {
        company = companyRepository.save(company);
    }

    @Test
    void shouldCreateCompanySuccessfully() throws Exception {
        CompanyCreate companyCreate = new CompanyCreate("Company B", "new@companyB.com", "ABC street 456");

        mockMvc.perform(post("/api/v1/companies/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyCreate))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(companyCreate.getName()))
                .andExpect(jsonPath("$.data.email").value(companyCreate.getEmail()))
                .andExpect(jsonPath("$.data.address").value(companyCreate.getAddress()))
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void shouldGetAllCompanies() throws Exception {
        mockMvc.perform(get("/api/v1/companies/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value(company.getName()))
                .andExpect(jsonPath("$.data[0].email").value(company.getEmail()))
                .andExpect(jsonPath("$.data[0].address").value(company.getAddress()));
    }

    @Test
    void shouldGetCompanyById() throws Exception {
        mockMvc.perform(get("/api/v1/companies/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(company.getName()))
                .andExpect(jsonPath("$.data.email").value(company.getEmail()))
                .andExpect(jsonPath("$.data.address").value(company.getAddress()));
    }

    @Test
    void shouldDeleteCompany() throws Exception {
        mockMvc.perform(delete("/api/v1/companies/" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data").value("Company deleted successfully"));

        // check that company not found
        mockMvc.perform(get("/api/v1/companies/" + company.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message").value("Company not found with id: " + company.getId()))
                .andExpect(jsonPath("$.data").value((Object) null));
    }

    @Test
    void shouldUpdateExistingCompany() throws Exception {
        Company newCompanyData = new Company("New Name", "new_email@email.com", "new address");

        mockMvc.perform(put("/api/v1/companies/" + company.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCompanyData))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.errors").value((Object) null))
                .andExpect(jsonPath("$.data.name").value(newCompanyData.getName()))
                .andExpect(jsonPath("$.data.email").value(newCompanyData.getEmail()))
                .andExpect(jsonPath("$.data.address").value(newCompanyData.getAddress()))
                .andExpect(jsonPath("$.data.id").value(company.getId().toString()));
    }
}
