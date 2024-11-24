package org.project_management.application.services.Invitation;

import org.project_management.application.dto.Invitation.InvitationRequest;
import org.project_management.application.dto.Invitation.UpdateInvitation;
import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.domain.abstractions.InvitationRepository;
import org.project_management.domain.abstractions.RoleRepository;
import org.project_management.domain.abstractions.WorkspaceRepository;
import org.project_management.domain.entities.invitation.Invitation;
import org.project_management.domain.entities.role.Role;
import org.project_management.domain.entities.workspace.Workspace;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RoleRepository roleRepository;

    public InvitationServiceImpl(InvitationRepository invitationRepository, WorkspaceRepository workspaceRepository, RoleRepository roleRepository) {
        this.invitationRepository = invitationRepository;
        this.workspaceRepository = workspaceRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Invitation save(InvitationRequest invitationRequest) {
        Optional<Invitation> invitationExist = invitationRepository.findByEmailandWorkspaceId(invitationRequest.getRecipientEmail(), invitationRequest.getWorkspaceId());
        if (invitationExist.isPresent()) {
            throw new UnableToSaveResourceException("Invitation already exist");
        }
        Workspace workspace = workspaceRepository.findById(invitationRequest.getWorkspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with id: " + invitationRequest.getWorkspaceId()));
        Role role = roleRepository.findById(invitationRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + invitationRequest.getRoleId()));
        Invitation invitation = new Invitation(invitationRequest.getRecipientEmail(), workspace, role);
        return invitationRepository.save(invitation);

    }

    @Override
    public Invitation update(UpdateInvitation updateInvitation) {
        Invitation invitation = invitationRepository.findById(updateInvitation.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found with id: " + updateInvitation.getId()));
        Workspace workspace = workspaceRepository.findById(updateInvitation.getWorkspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found with id: " + updateInvitation.getWorkspaceId()));
        Role role = roleRepository.findById(updateInvitation.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + updateInvitation.getRoleId()));

        invitation.setWorkspace(workspace);
        invitation.setRole(role);
        invitation.setAccepted(updateInvitation.isAccepted());

        return invitationRepository.update(invitation);
    }

    @Override
    public Optional<Invitation> findById(UUID invitationId) {
        return this.invitationRepository.findById(invitationId);
    }

    @Override
    public Optional<Invitation> findByEmailandWorkspaceId(String email, UUID workspaceId) {
        return invitationRepository.findByEmailandWorkspaceId(email, workspaceId);
    }

    @Override
    public Invitation updateInvitationStatus(String email, UUID workspaceId, boolean status) {
        Optional<Invitation> invitation = invitationRepository.findByEmailandWorkspaceId(email, workspaceId);
        if (invitation.isPresent() && !invitation.get().isAccepted()) {

            invitation.get().setAccepted(status);
            return invitationRepository.update(invitation.get());
        }
        throw new ResourceNotFoundException("Invitation not found with email: " + email + " and workspace id: " + workspaceId.toString());
    }

    @Override
    public void delete(UUID id) {
        invitationRepository.delete(id);
    }

    @Override
    public List<Invitation> findAll() {
        return invitationRepository.findAll();
    }
}
