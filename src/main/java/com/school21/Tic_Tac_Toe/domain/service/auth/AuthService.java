package com.school21.Tic_Tac_Toe.domain.service.auth;

import com.school21.Tic_Tac_Toe.domain.model.token.Token;

public interface AuthService {
    void register(String login, String password);

    Token login(String login, String password);

    Token refreshAccessToken(String refreshToken);

    Token refreshRefreshToken(String refreshToken);

    JwtAuthentication getAuthentication(String accessToken);

    JwtAuthentication getRefreshAuthentication(String refreshToken);
}