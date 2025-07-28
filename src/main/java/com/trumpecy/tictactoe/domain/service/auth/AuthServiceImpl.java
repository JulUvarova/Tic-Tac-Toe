package com.trumpecy.tictactoe.domain.service.auth;

import com.trumpecy.tictactoe.datasource.repository.token.RefreshTokenRepository;
import com.trumpecy.tictactoe.domain.model.token.Token;
import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.domain.service.user.UserService;
import com.trumpecy.tictactoe.exception.InvalidTokenException;
import com.trumpecy.tictactoe.exception.InvalidUserDataException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final RefreshTokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(String login, String password) {
        userService.create(login, passwordEncoder.encode(password));
    }

    @Override
    public Token login(String login, String password) {
        User user = userService.getByLogin(login);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserDataException("Invalid credentials");
        }
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        tokenRepository.save(user.getId(), refreshToken);
        return new Token(accessToken, refreshToken);
    }

    @Override
    public Token refreshAccessToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid token");
        }
        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        UUID id = UUID.fromString(claims.getSubject());
        String saveRefreshToken = tokenRepository.getById(id);
        if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
            User user = userService.getById(id);
            String accessToken = jwtProvider.generateAccessToken(user);
            return new Token(accessToken, null);
        } else {
            throw new InvalidTokenException("Invalid refresh token");
        }
    }

    @Override
    public Token refreshRefreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid token");
        }
        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        UUID id = UUID.fromString(claims.getSubject());
        String saveRefreshToken = tokenRepository.getById(id);
        if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
            User user = userService.getById(id);
            String accessToken = jwtProvider.generateAccessToken(user);
            String newRefreshToken = jwtProvider.generateRefreshToken(user);
            tokenRepository.save(id, newRefreshToken);
            return new Token(accessToken, newRefreshToken);
        } else {
            throw new InvalidTokenException("Invalid token");
        }
    }

    @Override
    public JwtAuthentication getAuthentication(String accessToken) {
        if (!jwtProvider.validateAccessToken(accessToken)) {
            return null;
        }
        Claims claims = jwtProvider.getAccessClaims(accessToken);
        return JwtUtil.create(claims);
    }

    @Override
    public JwtAuthentication getRefreshAuthentication(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            return null;
        }
        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        return JwtUtil.create(claims);
    }
} 