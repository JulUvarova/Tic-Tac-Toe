package com.school21.Tic_Tac_Toe.web.security.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshJwtRequest {
    private String refreshToken;
}