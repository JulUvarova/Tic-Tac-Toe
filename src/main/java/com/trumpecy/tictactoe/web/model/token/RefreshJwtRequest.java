package com.trumpecy.tictactoe.web.model.token;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshJwtRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}