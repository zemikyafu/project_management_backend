package org.project_management.application.services.workspace;

import jakarta.servlet.http.HttpServletRequest;
import org.project_management.application.dto.workspace.WorkspaceUserCreate;
import org.project_management.application.dto.workspace.WorkspaceUserMapper;
import org.project_management.application.dto.workspace.WorkspaceUserUpdate;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.domain.abstractions.AuthRepository;
import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.abstractions.WorkspaceUserRepository;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.project_management.presentation.config.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkspaceUserServiceImpl implements WorkspaceUserService{
    private final WorkspaceUserRepository workspaceUserRepository;
    private final RoleRepository roleRepository;
    private final WorkspaceRepository workspaceRepository;
    private final JwtAuthFilter jwtAuthFilter;
    private final HttpServletRequest request;
    private final AuthRepository authRepository;

    @Autowired
    public WorkspaceUserServiceImpl(WorkspaceUserRepository workspaceUserRepository,RoleRepository roleRepository, WorkspaceRepository workspaceRepository,JwtAuthFilter jwtAuthFilter,HttpServletRequest request,AuthRepository authRepository) {
        this.workspaceUserRepository = workspaceUserRepository;
        this.roleRepository = roleRepository;
        this.workspaceRepository = workspaceRepository;
        this.jwtAuthFilter = jwtAuthFilter;
        this.request = request;
        this.authRepository = authRepository;
    }


    @Override
    public WorkspaceUser save(WorkspaceUserCreate createDTO) {

        String userEmail = jwtAuthFilter.getUserEmailFromToken(request);
        User user = authRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findById(createDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Workspace workspace = workspaceRepository.findById(createDTO.getWorkspaceId())
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with id: " + createDTO.getWorkspaceId()));

        WorkspaceUser workspaceUser = WorkspaceUserMapper.toEntity(createDTO);
        workspaceUser.setUser(user);
        workspaceUser.setRole(role);
        workspaceUser.setWorkspace(workspace);
        return workspaceUserRepository.save(workspaceUser);
    }

    @Override
    public List<WorkspaceUser> findByWorkspaceId(UUID workspaceId) {
        return workspaceUserRepository.findByWorkspaceId(workspaceId);
    }

    @Override
    public Optional<WorkspaceUser> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId) {
        return workspaceUserRepository.findByWorkspaceIdAndUserId(workspaceId,userId);
    }

    @Override
    public WorkspaceUser update(WorkspaceUserUpdate updateDTO) {
        Role updatedRole = roleRepository.findById(updateDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (updatedRole == null) {
            throw new IllegalArgumentException("Role not found with id: " + updateDTO.getRoleId());
        }
        WorkspaceUser updatedWorkspaceUser = WorkspaceUserMapper.toEntity(updateDTO);
        updatedWorkspaceUser.setRole(updatedRole);
        return workspaceUserRepository.save(updatedWorkspaceUser);
    }


    @Override
    public void deleteByWorkspaceIdAndUserId(UUID workspaceId, UUID userId) {
        workspaceUserRepository.deleteByWorkspaceIdAndUserId(workspaceId,userId);
    }
}
