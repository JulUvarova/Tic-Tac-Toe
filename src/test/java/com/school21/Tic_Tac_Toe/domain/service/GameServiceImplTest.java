package com.school21.Tic_Tac_Toe.domain.service;

import com.school21.Tic_Tac_Toe.datasource.repository.GameRepository;
import com.school21.Tic_Tac_Toe.domain.model.BoardModel;
import com.school21.Tic_Tac_Toe.domain.model.GameConstant;
import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import com.school21.Tic_Tac_Toe.domain.model.GameStatus;
import com.school21.Tic_Tac_Toe.exception.GameNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private static GameModel game;
    private BoardModel nextBoard;

    @BeforeAll
    static void setUp() {
        game = new GameModel();
    }

    @BeforeEach
    void setUpGame() {
        nextBoard = new BoardModel();
    }

    @Test
    void getNextMove_AgentMakesMove() {
        BoardModel userBoard = new BoardModel(new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        });
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        GameModel result = gameService.getNextMove(game.getId(), userBoard);
        BoardModel resultBoard = result.getBoard();
        int oCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (resultBoard.getMatrix()[i][j] == GameConstant.PLAYER_O) {
                    oCount++;
                }
            }
        }
        assertEquals(1, oCount);
        verify(gameRepository).saveGame(game);
    }

    @Test
    void getNextMove_AgentMakesMoveAndWin() {
        BoardModel userBoard = new BoardModel(new int[][]{
                {1, 0, 2},
                {1, 0, 2},
                {0, 0, 0}
        });
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        GameModel result = gameService.getNextMove(game.getId(), userBoard);

        assertEquals(GameStatus.O_WINS, result.getBoard().checkGameStatus());
        verify(gameRepository).saveGame(game);
    }

    @Test
    void getNextMove_AgentMakesMoveForDraw() {
        BoardModel userBoard = new BoardModel(new int[][]{
                {0, 1, 0},
                {0, 2, 1},
                {1, 2, 1}
        });
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        GameModel result = gameService.getNextMove(game.getId(), userBoard);

        assertEquals(GameConstant.PLAYER_O, result.getBoard().getMatrix()[0][2]);
        assertEquals(GameStatus.IN_PROGRESS, result.getBoard().checkGameStatus());
        verify(gameRepository).saveGame(game);
    }

    @Test
    void getNextMove_GameOverAfterUserMove() {
        BoardModel userBoard = new BoardModel(new int[][]{
                {1, 1, 1},
                {0, 0, 0},
                {0, 0, 0}
        });
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        GameModel result = gameService.getNextMove(game.getId(), userBoard);

        assertEquals(GameStatus.X_WINS, result.getBoard().checkGameStatus());
        assertArrayEquals(userBoard.getMatrix(), result.getBoard().getMatrix());
    }

    @Test
    void getNextMove_InvalidGameId() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () -> gameService.getNextMove(game.getId(), new BoardModel()));
    }

    @Test
    public void testIsBoardValid_ValidMove() {
        game.setBoard(new BoardModel());
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_X;

        assertTrue(gameService.validateUserBoard(game.getId(), nextBoard));
    }

    @Test
    public void testIsBoardValid_Invalid_MultipleChanges() {
        game.setBoard(new BoardModel());
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_X;
        nextBoard.getMatrix()[0][1] = GameConstant.PLAYER_X;

        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));
    }

    @Test
    public void testIsBoardValid_Invalid_ChangeOccupiedCell() {
        game.setBoard(new BoardModel());
        game.getBoard().getMatrix()[0][0] = GameConstant.PLAYER_O;
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_X;

        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));
    }

    @Test
    public void testIsBoardValid_Invalid_ChangeToNotPlayerX() {
        game.setBoard(new BoardModel());
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_O;

        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));
    }

    @Test
    public void testIsBoardValid_Invalid_NullBoards() {
        game.setBoard(null);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));

        game.setBoard(new BoardModel());
        assertFalse(gameService.validateUserBoard(game.getId(), null));
    }

    @Test
    public void testIsGameOver_False() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        assertFalse(gameService.isGameOver(game.getId()));
    }

    @Test
    public void testIsGameOver_True() {
        game.setBoard(new BoardModel(new int[][]{
                {1, 1, 1},
                {0, 0, 0},
                {0, 0, 0}
        }));
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        assertTrue(gameService.isGameOver(game.getId()));
    }
}
