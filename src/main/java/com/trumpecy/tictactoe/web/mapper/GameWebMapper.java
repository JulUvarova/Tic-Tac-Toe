package com.trumpecy.tictactoe.web.mapper;

import com.trumpecy.tictactoe.domain.model.game.Game;
import com.trumpecy.tictactoe.web.model.game.GameDtoResponse;
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
        gameDto.setStartTime(gameModel.getStartTime());

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
