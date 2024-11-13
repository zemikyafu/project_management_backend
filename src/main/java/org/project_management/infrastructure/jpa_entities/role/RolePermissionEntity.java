package org.project_management.infrastructure.repositories.jpa_entities.role;

import jakarta.persistence.*;

@Entity
@Table(name = "role_permission")
public class RolePermissionEntity {
    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id",nullable = false)
    private RoleEntity role;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id",nullable = false)
    private PermissionEntity permission;

    public RolePermissionEntity() {
    }

    public RolePermissionEntity(RoleEntity role, PermissionEntity permission) {
        this.role = role;
        this.permission = permission;
        this.id = new RolePermissionId(role.getId(),permission.getId());
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public PermissionEntity getPermission() {
        return permission;
    }

    public void setPermission(PermissionEntity permission) {
        this.permission = permission;
    }
}
