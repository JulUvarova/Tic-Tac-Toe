package com.school21.Tic_Tac_Toe.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id = UUID.randomUUID();
    private String login;
    private String password;
}
