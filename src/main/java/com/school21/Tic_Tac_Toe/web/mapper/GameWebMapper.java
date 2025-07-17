package com.school21.Tic_Tac_Toe.web.mapper;

import com.school21.Tic_Tac_Toe.domain.model.game.Board;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.web.model.GameDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameWebMapper {
    public static GameDto toGameDto(Game gameModel) {
        GameDto gameDto = new GameDto();
        gameDto.setId(gameModel.getId());
        gameDto.setBoard(deepCopyBoard(gameModel.getBoard().getMatrix()));

        return gameDto;
    }

    public static Game toGameModel(GameDto gameDto) {
        Game gameModel = new Game();
        gameModel.setId(gameDto.getId());
        gameModel.setBoard(new Board(deepCopyBoard(gameDto.getBoard())));

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
