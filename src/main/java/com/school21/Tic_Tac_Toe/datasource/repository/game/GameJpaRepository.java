package com.school21.Tic_Tac_Toe.datasource.repository.game;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.datasource.model.UserRatioProjection;
import com.school21.Tic_Tac_Toe.datasource.model.UserStatsProjection;
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

    @Query("SELECT :userId AS userId, " +
            "   COALESCE(SUM(CASE " +
            "        WHEN (g.playerX = :userId AND g.status = 'X_WINS') OR " +
            "             (g.playerO = :userId AND g.status = 'O_WINS')" +
            "               THEN 1 ELSE 0 END), 0) AS wins, " +
            "   COALESCE(SUM(CASE " +
            "        WHEN (g.playerX = :userId AND g.status = 'O_WINS') OR " +
            "             (g.playerO = :userId AND g.status = 'X_WINS')" +
            "               THEN 1 ELSE 0 END), 0) AS losses, " +
            "   COALESCE(SUM(CASE " +
            "        WHEN g.status = 'DRAW' AND (g.playerX = :userId OR g.playerO = :userId)" +
            "               THEN 1 ELSE 0 END), 0) AS draws " +
            "FROM games g " +
            "WHERE :userId IN (g.playerX, g.playerO)"
    )
    UserStatsProjection getStatsByUserId(@Param("userId") UUID userId);

    @Query(value =
            "SELECT CAST(userId AS VARCHAR) AS userId," +
            "  COALESCE(SUM(win) * 1.0 / NULLIF(COUNT(*), 0), 0) AS winRatio " +
            "FROM (" +
            "  SELECT playerX AS userId, " +
            "     CASE WHEN status = 'X_WINS' THEN 1 ELSE 0 END AS win " +
            "  FROM games " +
            "  WHERE status IN ('X_WINS', 'O_WINS', 'DRAW') " +
            "    AND playerX IS NOT NULL " +
            "  UNION ALL " +
            "  SELECT playerO AS userId, " +
            "     CASE WHEN status = 'O_WINS' THEN 1 ELSE 0 END AS win " +
            "  FROM games " +
            "  WHERE status IN ('X_WINS', 'O_WINS', 'DRAW') " +
            "    AND playerO IS NOT NULL " +
            ") combined " +
            "GROUP BY userId " +
            "ORDER BY winRatio DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<UserRatioProjection> getLeaderBoard(@Param("limit") int limit);
}