package com.trumpecy.tictactoe.security;

import com.trumpecy.tictactoe.domain.service.auth.AuthService;
import com.trumpecy.tictactoe.domain.service.auth.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    public final String BEARER_PREFIX = "Bearer ";
    public final String HEADER_NAME = "Authorization";
    private final AuthService authService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (
                uri.equals("/auth/register")
                        || uri.equals("/auth/login")
                        || uri.equals("/auth/token")
                        || uri.equals("/auth/refresh")
        ) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HEADER_NAME);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        JwtAuthentication authentication = authService.getAuthentication(token);
        if (authentication == null) {
            log.warn("Invalid token provided for URI: {}", uri);
        } else {
            log.info("Authentication successful for user: {} {} and {}", authentication.getLogin(), authentication.getAuthorities(), uri);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
