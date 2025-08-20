package com.example.assignment.domain.user.dto.response;

import com.example.assignment.domain.user.entity.User;

import java.util.List;

public record UserResponseDto(
        String username,
        String nickname,
        List<RoleDto> roles
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUsername(),
                user.getNickname(),
                List.of(RoleDto.from(user.getRole()))
        );
    }
}
