package com.school21.Tic_Tac_Toe.datasource.mapper;

import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.domain.model.BoardModel;
import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameDataMapper {
    public static GameEntity toEntity(GameModel gameModel) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameModel.getId());
        gameEntity.setBoard(toBoardEntity(gameModel.getBoard()));

        return gameEntity;
    }

    public static GameModel toModel(GameEntity gameEntity) {
        GameModel gameModel = new GameModel();
        gameModel.setId(gameEntity.getId());
        gameModel.setBoard(toBoardModel(gameEntity.getBoard()));

        return gameModel;
    }

    private static String toBoardEntity(BoardModel boardModel) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < boardModel.getMatrix().length; i++) {
            for (int j = 0; j < boardModel.getMatrix()[i].length; j++) {
                stringBuilder.append(boardModel.getMatrix()[i][j]);
            }
        }
        return stringBuilder.toString();
    }

    private static BoardModel toBoardModel(String boardEntity) {
        BoardModel boardModel = new BoardModel();
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
