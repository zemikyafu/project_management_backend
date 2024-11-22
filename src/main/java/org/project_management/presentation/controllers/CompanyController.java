package org.project_management.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.project_management.application.dto.Company.CompanyCreate;
import org.project_management.application.dto.Company.CompanyMapper;
import org.project_management.application.dto.Company.CompanyUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.services.CompanyService;
import org.project_management.domain.entities.company.Company;
import org.project_management.presentation.shared.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company")
@Tag(name = "Company", description = "Company management")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "Create a new company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Company created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "409", description = "Company already exists with provided name or email"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to save company")
    })
    @PostMapping("/")
    public ResponseEntity<GlobalResponse<Company>> saveCompany(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New company information", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompanyCreate.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Muppets inc.\", \"email\": \"kermit.frog@muppets.com\", \"address\": \"Sesame street 123\" }")
                    ))
            @RequestBody @Valid CompanyCreate newCompany) {
        Company company = CompanyMapper.toCompany(newCompany);
        Company savedCompany = companyService.save(company);

        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), savedCompany), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete existing company by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Company not found with provided ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to delete company")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteCompany(
            @Parameter(description = "Company id", required = true, example = "47ceb1af-94e6-436b-9f43-91cbb6fb2120")
            @PathVariable UUID id
    ) {
        companyService.delete(id);

        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), "Company deleted successfully"));
    }

    @Operation(summary = "Get all companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "No companies found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to find companies")
    })
    @GetMapping("/")
    public ResponseEntity<GlobalResponse<List<Company>>> findAllCompanies() {
        List<Company> companies = companyService.findAll();

        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), companies));
    }

    @Operation(summary = "Get existing company by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company found"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Company not found with provided ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to find company")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<Company>> findCompanyById(
            @Parameter(description = "Company id", required = true, example = "47ceb1af-94e6-436b-9f43-91cbb6fb2120")
            @PathVariable UUID id
    ) {
        Company company = companyService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("company not found with ID: " + id.toString())
        );

        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), company));
    }

    @Operation(summary = "Update existing company by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, sign in to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden access, request privileges to access this resource"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error, unable to find company")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<Company>> updateCompany(
            @Parameter(description = "Company id", required = true, example = "47ceb1af-94e6-436b-9f43-91cbb6fb2120")
            @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Company data to be updated", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CompanyUpdate.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Muppets inc.\", \"email\": \"kermit.frog@muppets.com\", \"address\": \"Sesame street 123\" }")
                    ))
            @Valid @RequestBody CompanyUpdate companyUpdateDto
    ) {
        Company company = CompanyMapper.toCompany(companyUpdateDto);
        company.setId(id);
        Company updatedCompany = companyService.update(company);

        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedCompany));
    }
}
