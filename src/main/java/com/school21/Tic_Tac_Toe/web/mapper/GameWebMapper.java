package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.BoardModel;
import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import com.school21.Tic_Tac_Toe.web.model.GameDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameWebMapper {
    public static GameDto toGameDto(GameModel gameModel) {
        GameDto gameDto = new GameDto();
        gameDto.setId(gameModel.getId());
        gameDto.setBoard(deepCopyBoard(gameModel.getBoard().getMatrix()));

        return gameDto;
    }

    public static GameModel toGameModel(GameDto gameDto) {
        GameModel gameModel = new GameModel();
        gameModel.setId(gameDto.getId());
        gameModel.setBoard(new BoardModel(deepCopyBoard(gameDto.getBoard())));

        return gameModel;
    }

    private static int[][] deepCopyBoard(int[][] original) {
        if (original == null) return null;

        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}
