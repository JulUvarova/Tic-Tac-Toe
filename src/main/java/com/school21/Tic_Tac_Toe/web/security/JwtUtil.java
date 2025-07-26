package com.school21.Tic_Tac_Toe.web.security;

import com.school21.Tic_Tac_Toe.domain.model.user.Role;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtUtil {
    public static JwtAuthentication create(Claims claims) {
        UUID uuid = UUID.fromString(claims.getSubject());
        List<Role> roles = null;
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?>) {
            roles = ((List<?>) rolesObj).stream()
                    .map(Object::toString)
                    .map(Role::valueOf)
                    .collect(Collectors.toList());
        }
        return new JwtAuthentication(uuid, roles, true);
    }
} 