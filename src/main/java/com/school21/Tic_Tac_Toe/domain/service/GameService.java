package com.school21.Tic_Tac_Toe.domain.service;

import com.school21.Tic_Tac_Toe.domain.model.BoardModel;
import com.school21.Tic_Tac_Toe.domain.model.GameModel;

import java.util.UUID;

public interface GameService {
    /**
     * A method to get the next move of the current game using the Minimax algorithm.
     */
    GameModel getNextMove(UUID gameId, BoardModel userBoard);

    /**
     * A method to validate the current game board (check that previous moves haven't been changed).
     */
    boolean validateUserBoard(UUID gameId, BoardModel userBoard);

    /**
     * A method to check if the game has ended.
     */
    boolean isGameOver(UUID gameId);

    /**
     * Create new game.
     */
    GameModel createNewGame();
}