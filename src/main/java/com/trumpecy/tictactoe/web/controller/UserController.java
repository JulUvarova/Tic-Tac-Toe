package com.trumpecy.tictactoe.web.controller;

import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.domain.service.user.UserService;
import com.trumpecy.tictactoe.web.mapper.UserWebMapper;
import com.trumpecy.tictactoe.web.model.PageDto;
import com.trumpecy.tictactoe.web.model.user.UserDtoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
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
        User user = userService.getById(id);

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
    public ResponseEntity<PageDto<UserDtoResponse>> getAllUsers(
            @RequestParam int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Getting all users...");
        Page<User> userPage = userService.getAllUsersPageable(page, size);

        PageDto<UserDtoResponse> pageResponse = new PageDto<>(
                userPage.getContent().stream().map(UserWebMapper::toDto).toList(),
                userPage.getTotalPages(),
                userPage.getTotalElements(),
                userPage.getNumber()
        );
        log.info("Got {} users on {}nd page", pageResponse.getContent().size(), pageResponse.getNumber());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(pageResponse);
    }
}
