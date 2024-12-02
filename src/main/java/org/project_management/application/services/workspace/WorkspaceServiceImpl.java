package org.project_management.application.services.workspace;

import org.project_management.application.dto.workspace.WorkspaceCreate;
import org.project_management.application.dto.workspace.WorkspaceMapper;
import org.project_management.application.dto.workspace.WorkspaceUpdate;
import org.project_management.domain.abstractions.CompanyRepository;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.company.Company;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceServiceImpl implements WorkspaceService{
    private final WorkspaceRepository workspaceRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, CompanyRepository companyRepository) {
        this.workspaceRepository = workspaceRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Workspace save(WorkspaceCreate createDTO) {
        Company company = companyRepository.findById(createDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));;
        Workspace workspace = WorkspaceMapper.toEntity(createDTO);
        workspace.setCompany(company);
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

        Workspace updatedWorkspace = WorkspaceMapper.toEntity(updateDTO);

        if (updatedWorkspace.getName() != null) {
            existingWorkspace.setName(updatedWorkspace.getName());
        }
        if (updatedWorkspace.getDescription() != null) {
            existingWorkspace.setDescription(updatedWorkspace.getDescription());
        }
        return workspaceRepository.save(existingWorkspace);
    }

    @Override
    public void deleteByIdAndCompanyId(UUID workspaceId, UUID companyId) {
        workspaceRepository.deleteByIdAndCompanyId(workspaceId,companyId);
    }
}
