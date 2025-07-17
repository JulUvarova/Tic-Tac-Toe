package com.school21.Tic_Tac_Toe.datasource.repository.game;

import com.school21.Tic_Tac_Toe.domain.model.game.Game;

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
}
