package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.web.model.GameDtoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameWebMapper {
    public static GameDtoResponse toGameResponseDto(Game gameModel) {
        GameDtoResponse gameDto = new GameDtoResponse();
        gameDto.setId(gameModel.getId());
        gameDto.setBoard(deepCopyBoard(gameModel.getBoard().getMatrix()));
        gameDto.setPlayerO(gameModel.getPlayerO());
        gameDto.setPlayerX(gameModel.getPlayerX());
        gameDto.setStatus(gameModel.getStatus());
        gameDto.setCurrentPlayer(gameModel.getCurrentPlayer());

        return gameDto;
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
