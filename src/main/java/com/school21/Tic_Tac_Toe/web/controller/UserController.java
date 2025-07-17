package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.model.user.User;
import com.school21.Tic_Tac_Toe.domain.service.user.UserService;
import com.school21.Tic_Tac_Toe.web.mapper.GameWebMapper;
import com.school21.Tic_Tac_Toe.web.mapper.UserWebMapper;
import com.school21.Tic_Tac_Toe.web.model.UserDto;
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
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        log.info("Finding user {}...", id);
        User user = userService.getUserById(id);

        log.info("User {} {} is found", id, user.getLogin());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(UserWebMapper.toDto(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Getting all users...");
        List<UserDto> users = userService.getAllUsers().stream().map(UserWebMapper::toDto).collect(Collectors.toList());

        log.info("Get {} users",users.size());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(users);
    }
}
