package com.trumpecy.tictactoe.domain.service.game;

import com.trumpecy.tictactoe.domain.model.game.Game;
import com.trumpecy.tictactoe.domain.model.stats.UserRatio;
import com.trumpecy.tictactoe.domain.model.stats.UserStats;
import com.trumpecy.tictactoe.web.model.game.OpponentType;

import java.util.List;
import java.util.UUID;

public interface GameService {
    /**
     * A method to get the next move of the current game using the Minimax algorithm.
     */
    Game getNextMove(UUID gameId, UUID userId, int[][] userBoard);

    /**
     * Create new game.
     */
    Game createNewGame(UUID userId, OpponentType opponent);

    /**
     * Find game by id.
     */
    Game getGameById(UUID id);

    /**
     * A method to join user to game. Set user as a playerO and game status IN_PROGRESS
     */
    Game joinGame(UUID gameId, UUID userId);

    /**
     * A method to get list of available (WAITING) games. It excludes games with userId
     */
    List<Game> getAvailableGamesForUserId(UUID userId);

    /**
     * A method to get list of current (WAITING, IN_PROGRESS) games for user with userId
     */
    List<Game> getCurrentGamesByUserId(UUID userId);

    /**
     * A method to get list of completed (O_WINS, X_WINS, DRAW) games for user with userId
     */
    List<Game> getCompletedGamesByUserId(UUID userId);

    /**
     * A method to get amount of wins, losses, draws and win ratio for user
     */
    UserStats getUserStats(UUID userId);

    /**
     * A method to get sorted leaderboard with custom size
     */
    List<UserRatio> getLeaderBoard(int limit);
}