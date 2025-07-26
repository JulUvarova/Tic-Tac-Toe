package com.school21.Tic_Tac_Toe.web.security;

import com.school21.Tic_Tac_Toe.domain.model.user.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class JwtAuthentication implements Authentication {
    private final UUID uuid;
    private final List<Role> roles;
    private boolean authenticated;

    public JwtAuthentication(UUID uuid, List<Role> roles, boolean authenticated) {
        this.uuid = uuid;
        this.roles = roles;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return uuid;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return uuid.toString();
    }
} 