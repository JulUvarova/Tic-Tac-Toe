package com.school21.Tic_Tac_Toe.web.security;

import com.school21.Tic_Tac_Toe.domain.service.user.UserService;
import com.school21.Tic_Tac_Toe.exception.InvalidUserDataException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthFilter extends GenericFilterBean {
    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // без авторизации
        String path = httpRequest.getRequestURI();
        if (path.startsWith("/auth/register") || path.startsWith("/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new InvalidUserDataException("Missing or invalid Authorization header");
        }
        String base64Credentials = authHeader.substring("Basic".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] parts = credentials.split(":", 2);
        if (parts.length != 2) {
            throw new InvalidUserDataException("Malformed credentials");
        }
        userService.login(parts[0], parts[1]);

        chain.doFilter(request, response);
    }
}
