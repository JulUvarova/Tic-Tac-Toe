package com.school21.Tic_Tac_Toe.domain.service.user;

import com.school21.Tic_Tac_Toe.domain.model.user.User;
import com.school21.Tic_Tac_Toe.exception.InvalidUserDataException;
import com.school21.Tic_Tac_Toe.exception.UserAlreadyExistsException;
import com.school21.Tic_Tac_Toe.web.security.JwtAuthentication;
import com.school21.Tic_Tac_Toe.web.security.JwtProvider;
import com.school21.Tic_Tac_Toe.web.security.JwtUtil;
import com.school21.Tic_Tac_Toe.web.security.token.JwtRequest;
import com.school21.Tic_Tac_Toe.web.security.token.JwtResponse;
import com.school21.Tic_Tac_Toe.web.security.token.RefreshJwtRequest;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(String login, String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new UserAlreadyExistsException(String.format("User %s already exists", login));
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
    }

    @Override
    public UUID login(String login, String password) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new InvalidUserDataException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserDataException("Invalid credentials");
        }
        return user.getId();
    }


    @Override
    public JwtResponse login(JwtRequest request) {
        User user = userService.findByLogin(request.getLogin());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid login or password");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshAccessToken(RefreshJwtRequest request) {
        if (!jwtProvider.validateRefreshToken(request.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }
        Claims claims = jwtProvider.getClaims(request.getRefreshToken());
        JwtAuthentication authentication = JwtUtil.create(claims);
        User user = userService.findUserById((java.util.UUID) authentication.getPrincipal());
        String accessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse("Bearer", accessToken, request.getRefreshToken());
    }

    @Override
    public JwtResponse refreshRefreshToken(RefreshJwtRequest request) {
        if (!jwtProvider.validateRefreshToken(request.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }
        Claims claims = jwtProvider.getClaims(request.getRefreshToken());
        JwtAuthentication authentication = JwtUtil.create(claims);
        User user = userService.findById((java.util.UUID) authentication.getPrincipal());
        String refreshToken = jwtProvider.generateRefreshToken(user);
        String accessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse("Bearer", accessToken, refreshToken);
    }

    @Override
    public JwtAuthentication getAuthentication(String token) {
        if (!jwtProvider.validateAccessToken(token)) {
            throw new RuntimeException("Invalid access token");
        }
        Claims claims = jwtProvider.getClaims(token);
        JwtAuthentication authentication = JwtUtil.create(claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
} 