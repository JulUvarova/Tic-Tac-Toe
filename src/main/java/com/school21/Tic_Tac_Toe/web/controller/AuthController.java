package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.service.user.UserService;
import com.school21.Tic_Tac_Toe.exception.InvalidUserDataException;
import com.school21.Tic_Tac_Toe.web.model.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Registrate new user",
            description = "Registration method that takes a SignUpRequest and returns a registration success status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/register")
    public ResponseEntity<Boolean> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        if (userService.register(signUpRequest.getLogin(), signUpRequest.getPassword())) {
            log.info("User registered successfully");
            return ResponseEntity.ok(true);
        }
        log.info("User didn't register");
        return ResponseEntity.badRequest().body(false);
    }

    @Operation(summary = "Authorize user",
            description = "Authorization method that takes the login and password in the header as base64(login:password) and returns the user's UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "Invalid request data")
    })
    @PostMapping("/login")
    public ResponseEntity<UUID> login(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new InvalidUserDataException("Missing or invalid Authorization header");
        }
        // "Basic base64(login:password)"
        String base64Credentials = authHeader.substring("Basic".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] parts = credentials.split(":", 2);
        if (parts.length != 2) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UUID userId = userService.login(parts[0], parts[1]);
        return ResponseEntity.ok(userId);
    }
}
