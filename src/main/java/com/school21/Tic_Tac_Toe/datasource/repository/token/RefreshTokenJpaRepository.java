package com.school21.Tic_Tac_Toe.datasource.repository.token;

import com.school21.Tic_Tac_Toe.datasource.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<TokenEntity, UUID> {
}
