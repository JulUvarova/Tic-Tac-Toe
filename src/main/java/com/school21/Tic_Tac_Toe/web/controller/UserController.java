package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.model.user.User;
import com.school21.Tic_Tac_Toe.domain.service.user.UserService;
import com.school21.Tic_Tac_Toe.web.mapper.UserWebMapper;
import com.school21.Tic_Tac_Toe.web.model.user.UserDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable UUID id) {
        log.info("Finding user {}...", id);
        User user = userService.getUserById(id);

        log.info("User {} {} was found", id, user.getLogin());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(UserWebMapper.toDto(user));
    }

    @Operation(summary = "Get users' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
    })
    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        log.info("Getting all users...");
        List<UserDtoResponse> users = userService.getAllUsers().stream().map(UserWebMapper::toDto).collect(Collectors.toList());

        log.info("Got {} users", users.size());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(users);
    }
}
