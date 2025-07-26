package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.service.user.AuthService;
import com.school21.Tic_Tac_Toe.domain.service.user.UserService;
import com.school21.Tic_Tac_Toe.exception.InvalidUserDataException;
import com.school21.Tic_Tac_Toe.web.model.JwtRequest;
import com.school21.Tic_Tac_Toe.web.model.JwtResponse;
import com.school21.Tic_Tac_Toe.web.model.RefreshJwtRequest;
import com.school21.Tic_Tac_Toe.web.model.user.SignUpRequest;
import com.school21.Tic_Tac_Toe.web.security.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
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
        userService.register(signUpRequest.getLogin(), signUpRequest.getPassword());
        log.info("User registered successfully");
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Authorize user",
            description = "Authorization method that takes the login and password in the header as base64(login:password) and returns the user's UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "Invalid request data")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse response = authService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshRefreshToken(@RequestBody RefreshJwtRequest request) {
        JwtResponse response = authService.refreshRefreshToken(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<JwtAuthentication> getMe(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);
        JwtAuthentication authentication = authService.getAuthentication(token);
        return ResponseEntity.ok(authentication);
    }
}
