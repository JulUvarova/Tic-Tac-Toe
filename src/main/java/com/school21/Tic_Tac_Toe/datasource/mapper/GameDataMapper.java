package com.school21.Tic_Tac_Toe.datasource.mapper;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.domain.model.game.Board;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
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
