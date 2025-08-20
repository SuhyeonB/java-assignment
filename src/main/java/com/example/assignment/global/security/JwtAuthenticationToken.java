package com.example.assignment.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;
    private final String credentials;

    public JwtAuthenticationToken(AuthUser authUser, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.authUser = authUser;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authUser;
    }
}
