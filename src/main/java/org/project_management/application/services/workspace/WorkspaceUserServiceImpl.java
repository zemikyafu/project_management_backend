package org.project_management.application.services.workspace;

import org.project_management.application.dto.workspace.WorkspaceUserCreate;
import org.project_management.application.dto.workspace.WorkspaceUserMapper;
import org.project_management.application.dto.workspace.WorkspaceUserUpdate;
import org.project_management.application.services.AccessControl.RoleService;
import org.project_management.application.services.User.UserService;
import org.project_management.domain.abstractions.WorkspaceUserRepository;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.user.User;
import org.project_management.domain.entities.workspace.Workspace;
import org.project_management.domain.entities.workspace.WorkspaceUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkspaceUserServiceImpl implements WorkspaceUserService{
    private final WorkspaceUserRepository workspaceUserRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceUserServiceImpl(WorkspaceUserRepository workspaceUserRepository, UserService userService, RoleService roleService,WorkspaceService workspaceService) {
        this.workspaceUserRepository = workspaceUserRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.workspaceService = workspaceService;
    }


    @Override
    public WorkspaceUser save(WorkspaceUserCreate createDTO) {
        User user = userService.findById(createDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + createDTO.getUserId()));
        Role role = roleService.findById(createDTO.getRoleId());
        if (role == null) {
            throw new IllegalArgumentException("Role not found with id: " + createDTO.getRoleId());
        }
        Workspace workspace = workspaceService.findById(createDTO.getWorkspaceId())
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found with id: " + createDTO.getWorkspaceId()));

        WorkspaceUser workspaceUser = WorkspaceUserMapper.toEntity(createDTO, user, role, workspace);
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
        WorkspaceUser existingWorkspaceUser = workspaceUserRepository.findById(updateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("WorkspaceUser not found with id: " + updateDTO.getId()));

        Role updatedRole = roleService.findById(updateDTO.getRoleId());
        if (updatedRole == null) {
            throw new IllegalArgumentException("Role not found with id: " + updateDTO.getRoleId());
        }
        WorkspaceUser updatedWorkspaceUser = WorkspaceUserMapper.toEntity(updateDTO, existingWorkspaceUser, updatedRole);
        return workspaceUserRepository.save(updatedWorkspaceUser);
    }


    @Override
    public void deleteByWorkspaceIdAndUserId(UUID workspaceId, UUID userId) {
        workspaceUserRepository.deleteByWorkspaceIdAndUserId(workspaceId,userId);
    }
}
