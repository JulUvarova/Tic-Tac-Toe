package com.trumpecy.tictactoe.domain.model.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
