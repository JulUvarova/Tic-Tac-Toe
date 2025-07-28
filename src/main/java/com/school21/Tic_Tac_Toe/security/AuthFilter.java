package com.school21.Tic_Tac_Toe.security;

import com.school21.Tic_Tac_Toe.domain.service.auth.AuthService;
import com.school21.Tic_Tac_Toe.domain.service.auth.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends GenericFilterBean {
    private final AuthService authService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String uri = httpRequest.getRequestURI();
        log.info("AuthFilter processing URI: {}", uri);
        if (
                uri.equals("/auth/register")
                        || uri.equals("/auth/login")
                        || uri.equals("/auth/token")
                        || uri.equals("/auth/refresh")
        ) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        log.info("Authorization header: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No valid Authorization header found for URI: {}", uri);
            chain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        JwtAuthentication authentication = authService.getAuthentication(token);
        if (authentication == null) {
            log.warn("Invalid token provided for URI: {}", uri);
        } else {
            log.info("Authentication successful for user: {}", authentication.getLogin());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
