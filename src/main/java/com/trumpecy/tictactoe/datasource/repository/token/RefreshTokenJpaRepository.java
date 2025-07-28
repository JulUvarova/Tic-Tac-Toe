package com.trumpecy.tictactoe.datasource.repository.token;

import com.trumpecy.tictactoe.datasource.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<TokenEntity, UUID> {
}
