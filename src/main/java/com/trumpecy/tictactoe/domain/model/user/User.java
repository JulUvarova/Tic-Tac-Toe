package com.trumpecy.tictactoe.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id = UUID.randomUUID();
    private String login;
    private String password;
    private Set<Role> roles = new HashSet<>(Set.of(Role.USER));
}
