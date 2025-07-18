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
        gameDto.setPlayerO(gameModel.getPlayerO());
        gameDto.setPlayerX(gameModel.getPlayerX());
        gameDto.setStatus(gameModel.getStatus());
        gameDto.setCurrentPlayer(gameModel.getCurrentPlayer());

        return gameDto;
    }

    public static Game toGameModel(GameDto gameDto) {
        Game gameModel = new Game();
        gameModel.setId(gameDto.getId());
        gameModel.setBoard(new Board(deepCopyBoard(gameDto.getBoard())));
        gameModel.setPlayerO(gameDto.getPlayerO());
        gameModel.setPlayerX(gameDto.getPlayerX());
        gameModel.setStatus(gameDto.getStatus());
        gameModel.setCurrentPlayer(gameDto.getCurrentPlayer());

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
