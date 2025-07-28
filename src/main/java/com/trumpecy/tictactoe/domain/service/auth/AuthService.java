package com.trumpecy.tictactoe.domain.service.auth;

import com.trumpecy.tictactoe.domain.model.token.Token;

public interface AuthService {
    void register(String login, String password);

    Token login(String login, String password);

    Token refreshAccessToken(String refreshToken);

    Token refreshRefreshToken(String refreshToken);

    JwtAuthentication getAuthentication(String accessToken);

    JwtAuthentication getRefreshAuthentication(String refreshToken);
}