package com.example.assignment.global.security;

import com.example.assignment.domain.user.role.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final Long userId;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String username, Role role) {
        this.userId = userId;
        this.username = username;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}
