package com.trumpecy.tictactoe.domain.service.auth;

import com.trumpecy.tictactoe.domain.model.user.Role;
import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class JwtUtil {
    public static JwtAuthentication create(Claims claims) {
        UUID id = UUID.fromString(claims.getSubject());
        String login = claims.get("login", String.class);
        Set<Role> roles = getRoles(claims);
        return new JwtAuthentication(id, login, roles, true);
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
} 