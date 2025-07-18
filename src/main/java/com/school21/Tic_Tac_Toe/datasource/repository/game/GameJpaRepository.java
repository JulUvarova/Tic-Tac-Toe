package com.school21.Tic_Tac_Toe.datasource.repository.game;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameJpaRepository extends JpaRepository<GameEntity, UUID> {
    List<GameEntity> getGameEntityByStatusStartsWithAndPlayerONotContainsAndPlayerXNotContains(GameStatus gameStatus, UUID userId, UUID userId1);
}