package com.school21.Tic_Tac_Toe.web.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoResponse {
    private UUID id;
    private String login;
    private List<String> roles;
}