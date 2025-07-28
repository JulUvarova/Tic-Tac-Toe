package com.trumpecy.tictactoe.datasource.repository.token;

import com.trumpecy.tictactoe.datasource.model.TokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository tokenJpaRepository;

    @Override
    public void save(UUID id, String refreshToken) {
        tokenJpaRepository.save(new TokenEntity(id, refreshToken));
    }

    @Override
    public String getById(UUID id) {
        Optional<TokenEntity> token = tokenJpaRepository.findById(id);
        return token.map(TokenEntity::getRefreshToken).orElse(null);
    }
}
