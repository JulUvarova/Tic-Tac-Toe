package com.trumpecy.tictactoe.datasource.mapper;

import com.trumpecy.tictactoe.datasource.model.GameEntity;
import com.trumpecy.tictactoe.domain.model.game.Board;
import com.trumpecy.tictactoe.domain.model.game.Game;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameDataMapper {
    public static GameEntity toEntity(Game gameModel) {
        if (gameModel == null) {
            return null;
        }

        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameModel.getId());
        gameEntity.setBoard(toBoardEntity(gameModel.getBoard()));
        gameEntity.setPlayerX(gameModel.getPlayerX());
        gameEntity.setPlayerO(gameModel.getPlayerO());
        gameEntity.setStatus(gameModel.getStatus());
        gameEntity.setCurrentPlayer(gameModel.getCurrentPlayer());
        gameEntity.setStartTime(gameModel.getStartTime());

        return gameEntity;
    }

    public static Game toModel(GameEntity gameEntity) {
        if (gameEntity == null) {
            return null;
        }

        Game gameModel = new Game();
        gameModel.setId(gameEntity.getId());
        gameModel.setBoard(toBoardModel(gameEntity.getBoard()));
        gameModel.setPlayerX(gameEntity.getPlayerX());
        gameModel.setPlayerO(gameEntity.getPlayerO());
        gameModel.setStatus(gameEntity.getStatus());
        gameModel.setCurrentPlayer(gameEntity.getCurrentPlayer());
        gameModel.setStartTime(gameEntity.getStartTime());

        return gameModel;
    }

    private static String toBoardEntity(Board boardModel) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < boardModel.getMatrix().length; i++) {
            for (int j = 0; j < boardModel.getMatrix()[i].length; j++) {
                stringBuilder.append(boardModel.getMatrix()[i][j]);
            }
        }
        return stringBuilder.toString();
    }

    private static Board toBoardModel(String boardEntity) {
        Board boardModel = new Board();
        int counter = 0;
        for (int i = 0; i < boardModel.getMatrix().length; i++) {
            for (int j = 0; j < boardModel.getMatrix()[i].length; j++) {
                char ch = boardEntity.charAt(counter);
                boardModel.getMatrix()[i][j] = Character.getNumericValue(ch);
                counter++;
            }
        }
        return boardModel;
    }
}
