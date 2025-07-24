package com.school21.Tic_Tac_Toe.datasource.repository.game;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameJpaRepository extends JpaRepository<GameEntity, UUID> {
    List<GameEntity> getGameEntityByStatusAndPlayerXNot(GameStatus gameStatus, UUID userId, Sort sort);

    @Query("SELECT g FROM games g " +
            "WHERE g.status IN :statusList " +
            "AND (g.playerX = :userId OR g.playerO = :userId)")
    List<GameEntity> findGamesByStatusInAndPlayer(@Param("statusList") List<GameStatus> statusList,
                                                  @Param("userId") UUID userId,
                                                  Sort sort);
}