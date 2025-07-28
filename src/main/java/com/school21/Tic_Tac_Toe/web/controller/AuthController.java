package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.service.auth.AuthService;
import com.school21.Tic_Tac_Toe.exception.InvalidTokenException;
import com.school21.Tic_Tac_Toe.domain.service.auth.JwtAuthentication;
import com.school21.Tic_Tac_Toe.web.mapper.TokenWebMapper;
import com.school21.Tic_Tac_Toe.web.model.token.JwtRequest;
import com.school21.Tic_Tac_Toe.web.model.token.JwtResponse;
import com.school21.Tic_Tac_Toe.web.model.token.RefreshJwtRequest;
import com.school21.Tic_Tac_Toe.web.model.user.SignUpRequest;
import com.school21.Tic_Tac_Toe.web.model.user.UserDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Register new user",
            description = "Registration method that takes a SignUpRequest and returns a registration success status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/register")
    public ResponseEntity<Boolean> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        log.info("New user is registering...");
        authService.register(signUpRequest.getLogin(), signUpRequest.getPassword());
        log.info("User {} registered successfully", signUpRequest.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @Operation(summary = "Authorize user",
            description = "Authorization method that takes the login and password in the header as base64(login:password) and returns the user's UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "Invalid request data")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid JwtRequest request) {
        log.info("User {} is authenticating...", request.getLogin());
        JwtResponse token = TokenWebMapper.toJwtResponse(authService.login(request.getLogin(), request.getPassword()));
        log.info("User {} authenticated successfully", request.getLogin());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(token);
    }


    @Operation(summary = "Refresh access token",
            description = "Refresh access token using refresh token. Returns new access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or expired refresh token")
    })
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody @Valid RefreshJwtRequest request) {
        UUID userId = getUserIdFromRefreshToken(request.getRefreshToken());
        log.info("User {} is asking for refresh access token...", userId);
        JwtResponse token = TokenWebMapper.toJwtResponse(authService.refreshAccessToken(request.getRefreshToken()));
        log.info("User {} got new access token", userId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(token);
    }

    @Operation(summary = "Refresh refresh token",
            description = "Refresh both access token and refresh token using current refresh token. Returns new access and refresh tokens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or expired refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshRefreshToken(@RequestBody @Valid RefreshJwtRequest request) {
        UUID userId = getUserIdFromRefreshToken(request.getRefreshToken());
        log.info("User {} is asking for refresh refresh token...", userId);
        JwtResponse token = TokenWebMapper.toJwtResponse(authService.refreshRefreshToken(request.getRefreshToken()));
        log.info("User {} got new refresh token", userId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(token);
    }

    @Operation(summary = "Get current user info",
            description = "Get information about the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UserDtoResponse> getMe() {
        log.info("User is asking about themself...");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth instanceof JwtAuthentication jwtAuth)) {
            throw new InvalidTokenException("Invalid token");
        }

        UserDtoResponse userInfo = new UserDtoResponse(
                (UUID) jwtAuth.getPrincipal(),
                jwtAuth.getLogin(),
                jwtAuth.getAuthorities().stream()
                        .map(Object::toString)
                        .toList()
        );
        log.info("Got user info: {}", userInfo);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(userInfo);
    }

    private UUID getUserIdFromRefreshToken(String refreshToken) {
        JwtAuthentication authentication = authService.getRefreshAuthentication(refreshToken);
        if (authentication == null) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        return UUID.fromString(authentication.getPrincipal().toString());
    }
}
