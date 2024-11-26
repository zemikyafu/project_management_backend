package org.project_management.application.services.workspace;

import org.project_management.application.dto.workspace.WorkspaceCreate;
import org.project_management.application.dto.workspace.WorkspaceMapper;
import org.project_management.application.dto.workspace.WorkspaceUpdate;
import org.project_management.application.services.Company.CompanyService;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkspaceServiceImpl implements WorkspaceService{

    private final WorkspaceRepository workspaceRepository;
    private final CompanyService companyService;


    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository,CompanyService companyService) {
        this.workspaceRepository = workspaceRepository;
        this.companyService = companyService;
    }


    @Override
    public Workspace save(WorkspaceCreate createDTO, UUID companyId) {

        Company company = companyService.findById(companyId);
        if (company == null) {
            throw new IllegalArgumentException("Company not found with id: " + createDTO.getCompanyId());
        }
        Workspace workspace = WorkspaceMapper.toEntity(createDTO, company);
        return workspaceRepository.save(workspace);
    }

    @Override
    public Optional<Workspace> findById(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId);
    }

    @Override
    public Optional<Workspace> findByIdAndCompanyId(UUID workspaceId, UUID companyId) {
        return workspaceRepository.findByIdAndCompanyId(workspaceId,companyId);
    }

    @Override
    public List<Workspace> findByCompanyId(UUID companyId) {
        return workspaceRepository.findByCompanyId(companyId);
    }

    @Override
    public Workspace update(WorkspaceUpdate updateDTO) {
        Workspace existingWorkspace = workspaceRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found"));
        Workspace updatedWorkspace = WorkspaceMapper.toEntity(updateDTO, existingWorkspace);
        return workspaceRepository.update(updatedWorkspace);


    }


    @Override
    public void deleteByIdAndCompanyId(UUID workspaceId, UUID companyId) {
        workspaceRepository.deleteByIdAndCompanyId(workspaceId,companyId);
    }
}
