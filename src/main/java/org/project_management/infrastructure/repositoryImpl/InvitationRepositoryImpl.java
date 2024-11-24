package org.project_management.infrastructure.repositoryImpl;

import org.project_management.application.exceptions.ResourceNotFoundException;
import org.project_management.application.exceptions.UnableToDeleteResourceException;
import org.project_management.application.exceptions.UnableToSaveResourceException;
import org.project_management.application.exceptions.UnableToUpdateResourceException;
import org.project_management.domain.abstractions.InvitationRepository;
import org.project_management.domain.entities.invitation.Invitation;
import org.project_management.infrastructure.jpa_repositories.JpaInvitationRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InvitationRepositoryImpl implements InvitationRepository {

    private final JpaInvitationRepository jpaInvitationRepository;

    public InvitationRepositoryImpl(JpaInvitationRepository jpaInvitationRepository) {
        this.jpaInvitationRepository = jpaInvitationRepository;
    }

    @Override
    public Invitation save(Invitation invitation) {
        try {
            return jpaInvitationRepository.save(invitation);
        } catch (Exception e) {
            throw new UnableToSaveResourceException("Error saving invitation");
        }
    }

    @Override
    public Optional<Invitation> findById(UUID invitationId) {
        return jpaInvitationRepository.findById(invitationId)
                .or(() -> {
                    throw new ResourceNotFoundException("Invitation not found with id: " + invitationId.toString());
                });
    }

    @Override
    public Optional<Invitation> findByEmailandWorkspaceId(String email, UUID workspaceId){
        return jpaInvitationRepository.findByEmailAndWorkspaceId(email, workspaceId).or(() -> {
                    throw new ResourceNotFoundException("Invitation not found with email: " + email + " and workspace id: " + workspaceId.toString());
                });
    }


    @Override
    public Invitation update(Invitation invitation) {
        Invitation invitationUpdate = jpaInvitationRepository.findById(invitation.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        invitationUpdate.setEmail(invitation.getEmail());
        invitationUpdate.setAccepted(invitation.isAccepted());
        invitationUpdate.setExpiredAt(invitation.getExpiredAt());
        invitationUpdate.setRole(invitation.getRole());
        invitationUpdate.setWorkspace(invitation.getWorkspace());
        try {
            return jpaInvitationRepository.save(invitationUpdate);
        } catch (Exception e) {
            throw new UnableToUpdateResourceException("Unable to update invitation");
        }

    }

    @Override
    public void delete(UUID id) {
        Invitation invitation = jpaInvitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));
        try {
            jpaInvitationRepository.delete(invitation);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Unable to delete invitation");
        }
    }

    @Override
    public List<Invitation> findAll() {
        return jpaInvitationRepository.findAll();
    }
}
