package com.trumpecy.tictactoe.datasource.repository.token;

import java.util.UUID;

public interface RefreshTokenRepository {
    void save(UUID id, String refreshToken);

    String getById(UUID id);
}
