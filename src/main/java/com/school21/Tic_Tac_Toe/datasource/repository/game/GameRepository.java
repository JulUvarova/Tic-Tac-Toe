package com.school21.Tic_Tac_Toe.datasource.repository.game;

import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserRatio;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserStats;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository {
    /**
     * A method to save the current game.
     */
    void saveGame(Game game);

    /**
     * A method to get the current game.
     */
    Optional<Game> findById(UUID id);

    /**
     * A method to get list game with status=WAITING without userId.
     */
    List<Game> getAvailableGamesForUser(UUID userId);

    /**
     * A method to get list of current (WAITING, IN_PROGRESS) games for user with userId
     */
    List<Game> getCurrentGamesByUserId(UUID userId);

    /**
     * A method to get list of completed (O_WINS, X_WINS, DRAW) games for user with userId
     */
    List<Game> getCompletedGamesByUserId(UUID userId);

    /**
     * A method to get amount of wins, losses, draws for user
     */
    UserStats getUserStats(UUID userId);

    /**
     * A method to get sorted leaderboard with custom size
     */
    List<UserRatio> getLeaderBoard(int limit);
}
