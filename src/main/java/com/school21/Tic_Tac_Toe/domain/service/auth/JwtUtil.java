package com.school21.Tic_Tac_Toe.domain.service.auth;

import com.school21.Tic_Tac_Toe.domain.model.user.Role;
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
        Set<Role> roles = getRoles(claims);
        return new JwtAuthentication(id, roles, true);
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