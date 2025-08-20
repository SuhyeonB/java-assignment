package com.example.assignment.domain.user.dto.response;

import com.example.assignment.domain.user.role.Role;

public record RoleDto(String role) {
    public static RoleDto from (Role role) {
        return new RoleDto(role.name());
    }
}
