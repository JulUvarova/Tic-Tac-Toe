package com.school21.Tic_Tac_Toe.datasource.mapper;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.domain.model.game.Board;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDataMapperTest {
    @Test
    public void testMapper() {
        Game gameModel = new Game();
        Board boardModel = new Board(new int[][]{
                {1, 1, 1},
                {2, 0, 2},
                {0, 0, 0}
        });
        gameModel.setBoard(boardModel);

        GameEntity gameEntity = GameDataMapper.toEntity(gameModel);

        Game gameAfterMapping = GameDataMapper.toModel(gameEntity);

        assertEquals(gameAfterMapping.getBoard(), boardModel);
    }

}