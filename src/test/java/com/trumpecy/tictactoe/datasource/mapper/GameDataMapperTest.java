package com.trumpecy.tictactoe.datasource.mapper;

import com.trumpecy.tictactoe.datasource.model.GameEntity;
import com.trumpecy.tictactoe.domain.model.game.Board;
import com.trumpecy.tictactoe.domain.model.game.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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