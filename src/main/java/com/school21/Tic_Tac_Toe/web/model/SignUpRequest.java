package com.school21.Tic_Tac_Toe.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String login;
    private String password;
}
