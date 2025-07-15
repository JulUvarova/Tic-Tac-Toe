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
        gameEntity.setBoard(gameModel.getBoard().getMatrix());

        return gameEntity;
    }

    public static GameModel toModel(GameEntity gameEntity) {
        GameModel gameModel = new GameModel();
        gameModel.setId(gameEntity.getId());
        gameModel.setBoard(new BoardModel(gameEntity.getBoard()));

        return gameModel;
    }
}
