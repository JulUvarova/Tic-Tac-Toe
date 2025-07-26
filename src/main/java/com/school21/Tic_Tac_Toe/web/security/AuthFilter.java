package com.school21.Tic_Tac_Toe.web.security;

import com.school21.Tic_Tac_Toe.domain.service.user.UserService;
import com.school21.Tic_Tac_Toe.exception.InvalidUserDataException;
import com.school21.Tic_Tac_Toe.security.JwtAuthentication;
import com.school21.Tic_Tac_Toe.security.JwtProvider;
import com.school21.Tic_Tac_Toe.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthFilter extends GenericFilterBean {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String uri = httpRequest.getRequestURI();
        if (uri.equals("/auth/register") || uri.equals("/auth/login") || uri.equals("/auth/token") || uri.equals("/auth/refresh")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        if (!jwtProvider.validateAccessToken(token)) {
            chain.doFilter(request, response);
            return;
        }
        Claims claims = jwtProvider.getClaims(token);
        JwtAuthentication authentication = JwtUtil.create(claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
