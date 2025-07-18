package com.school21.Tic_Tac_Toe.datasource.repository.game;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameJpaRepository extends JpaRepository<GameEntity, UUID> {
}