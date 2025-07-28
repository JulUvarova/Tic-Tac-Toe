package com.trumpecy.tictactoe.web.model.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String type;
    private String accessToken;
    private String refreshToken;
}