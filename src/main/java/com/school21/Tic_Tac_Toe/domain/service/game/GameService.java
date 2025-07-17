package com.school21.Tic_Tac_Toe.domain.service.game;

import com.school21.Tic_Tac_Toe.domain.model.game.Board;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;

import java.util.UUID;

public interface GameService {
    /**
     * A method to get the next move of the current game using the Minimax algorithm.
     */
    Game getNextMove(UUID gameId, Board userBoard);

    /**
     * A method to validate the current game board (check that previous moves haven't been changed).
     */
    boolean validateUserBoard(UUID gameId, Board userBoard);

    /**
     * A method to check if the game has ended.
     */
    boolean isGameOver(UUID gameId);

    /**
     * Create new game.
     */
    Game createNewGame();

    /**
     * Find game by id.
     */
    Game getGameById(UUID id);
}