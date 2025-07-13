package com.school21.Tic_Tac_Toe.datasource.repository;

import com.school21.Tic_Tac_Toe.domain.model.GameModel;

import java.util.Optional;
import java.util.UUID;

public interface GameRepository {
    /**
     * A method to save the current game.
     */
    void saveGame(GameModel game);

    /**
     * A method to get the current game.
     */
    Optional<GameModel> findById(UUID id);
}
