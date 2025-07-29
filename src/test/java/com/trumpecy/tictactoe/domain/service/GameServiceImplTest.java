package com.trumpecy.tictactoe.domain.service;

import com.trumpecy.tictactoe.datasource.repository.game.GameRepository;
import com.trumpecy.tictactoe.domain.model.game.Board;
import com.trumpecy.tictactoe.domain.model.game.Game;
import com.trumpecy.tictactoe.domain.model.game.GameConstant;
import com.trumpecy.tictactoe.domain.model.game.GameStatus;
import com.trumpecy.tictactoe.domain.model.stats.UserRatio;
import com.trumpecy.tictactoe.domain.model.stats.UserStats;
import com.trumpecy.tictactoe.domain.service.game.GameServiceImpl;
import com.trumpecy.tictactoe.exception.EntityNotFoundException;
import com.trumpecy.tictactoe.exception.InvalidGameIdException;
import com.trumpecy.tictactoe.exception.InvalidMoveException;
import com.trumpecy.tictactoe.web.model.game.OpponentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private static Game game;
    private static UUID playerXId;
    private static UUID playerOId;

    @BeforeAll
    static void setUp() {
        playerXId = UUID.randomUUID();
        playerOId = UUID.randomUUID();
        game = new Game();
    }

    @BeforeEach
    void setUpGame() {
        game.setBoard(new Board());
        game.setPlayerX(playerXId);
        game.setPlayerO(playerOId);
        game.setCurrentPlayer(playerXId);
        game.setStatus(GameStatus.IN_PROGRESS);
    }

    @Test
    void getNextMove_AgentMakesMove() {
        game.setPlayerO(GameConstant.MINIMAX_AGENT_UUID);
        game.setCurrentPlayer(playerXId);

        int[][] userBoard = new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        Game result = gameService.getNextMove(game.getId(), playerXId, userBoard);
        Board resultBoard = result.getBoard();
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
        game.setPlayerO(GameConstant.MINIMAX_AGENT_UUID);
        game.setCurrentPlayer(playerXId);

        game.setBoard(new Board(new int[][]{
                {1, 0, 2},
                {0, 0, 2},
                {0, 0, 0}
        }));
        int[][] userBoard = new int[][]{
                {1, 0, 2},
                {1, 0, 2},
                {0, 0, 0}
        };
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        Game result = gameService.getNextMove(game.getId(), playerXId, userBoard);

        assertEquals(GameStatus.O_WINS, result.getBoard().checkGameStatus());
        verify(gameRepository).saveGame(game);
    }

    @Test
    void getNextMove_AgentMakesMoveForDraw() {
        game.setPlayerO(GameConstant.MINIMAX_AGENT_UUID);
        game.setCurrentPlayer(playerXId);
        game.setBoard(new Board(new int[][]{
                {0, 1, 0},
                {0, 2, 1},
                {1, 2, 0}
        }));
        int[][] userBoard = new int[][]{
                {0, 1, 0},
                {0, 2, 1},
                {1, 2, 1}
        };
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        Game result = gameService.getNextMove(game.getId(), playerXId, userBoard);

        assertEquals(GameConstant.PLAYER_O, result.getBoard().getMatrix()[0][2]);
        assertEquals(GameStatus.IN_PROGRESS, result.getBoard().checkGameStatus());
        verify(gameRepository).saveGame(game);
    }

    @Test
    void getNextMove_GameOverAfterUserMove() {
        game.setCurrentPlayer(playerXId);
        game.setBoard(new Board(new int[][]{
                {1, 1, 0},
                {0, 0, 0},
                {0, 0, 0}
        }));
        int[][] userBoard = new int[][]{
                {1, 1, 1},
                {0, 0, 0},
                {0, 0, 0}
        };
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        Game result = gameService.getNextMove(game.getId(), playerXId, userBoard);

        assertEquals(GameStatus.X_WINS, result.getBoard().checkGameStatus());
        assertArrayEquals(userBoard, result.getBoard().getMatrix());
    }

    @Test
    void getNextMove_InvalidGameId() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.getNextMove(game.getId(), playerXId, new int[3][3]));
    }

    @Test
    void getNextMove_InvalidPlayerTurn() {
        game.setCurrentPlayer(playerOId);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        int[][] userBoard = new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        assertThrows(InvalidGameIdException.class, () ->
                gameService.getNextMove(game.getId(), playerXId, userBoard));
    }

    @Test
    void getNextMove_GameAlreadyOver() {
        game.setStatus(GameStatus.X_WINS);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        int[][] userBoard = new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        assertThrows(InvalidMoveException.class, () ->
                gameService.getNextMove(game.getId(), playerXId, userBoard));
    }

    @Test
    void getNextMove_InvalidMove() {
        game.setCurrentPlayer(playerXId);
        game.getBoard().getMatrix()[0][0] = GameConstant.PLAYER_O;
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        int[][] userBoard = new int[][]{
                {1, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        assertThrows(InvalidMoveException.class, () ->
                gameService.getNextMove(game.getId(), playerXId, userBoard));
    }

    @Test
    void createNewGame_WithComputer() {
        Game result = gameService.createNewGame(playerXId, OpponentType.COMPUTER);

        assertEquals(playerXId, result.getPlayerX());
        assertEquals(GameConstant.MINIMAX_AGENT_UUID, result.getPlayerO());
        assertEquals(playerXId, result.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, result.getStatus());
        verify(gameRepository).saveGame(result);
    }

    @Test
    void createNewGame_WithUser() {
        Game result = gameService.createNewGame(playerXId, OpponentType.USER);

        assertEquals(playerXId, result.getPlayerX());
        assertNull(result.getPlayerO());
        assertEquals(playerXId, result.getCurrentPlayer());
        assertEquals(GameStatus.WAITING, result.getStatus());
        verify(gameRepository).saveGame(result);
    }

    @Test
    void getGameById_Success() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        Game result = gameService.getGameById(game.getId());

        assertEquals(game, result);
    }

    @Test
    void getGameById_NotFound() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.getGameById(game.getId()));
    }

    @Test
    void joinGame_Success() {
        Game waitingGame = new Game();
        waitingGame.setPlayerX(playerXId);
        waitingGame.setStatus(GameStatus.WAITING);
        when(gameRepository.findById(waitingGame.getId())).thenReturn(Optional.of(waitingGame));

        Game result = gameService.joinGame(waitingGame.getId(), playerOId);

        assertEquals(playerOId, result.getPlayerO());
        assertEquals(playerXId, result.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, result.getStatus());
        verify(gameRepository).saveGame(result);
    }

    @Test
    void joinGame_GameNotFound() {
        when(gameRepository.findById(game.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.joinGame(game.getId(), playerOId));
    }

    @Test
    void joinGame_GameNotWaiting() {
        game.setStatus(GameStatus.IN_PROGRESS);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        assertThrows(InvalidGameIdException.class, () -> gameService.joinGame(game.getId(), playerOId));
    }

    @Test
    void joinGame_AlreadyHasPlayerO() {
        game.setStatus(GameStatus.WAITING);
        game.setPlayerO(playerOId);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        assertThrows(InvalidGameIdException.class, () -> gameService.joinGame(game.getId(), UUID.randomUUID()));
    }

    @Test
    void joinGame_SamePlayer() {
        game.setStatus(GameStatus.WAITING);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        assertThrows(InvalidGameIdException.class, () -> gameService.joinGame(game.getId(), playerXId));
    }

    @Test
    void getAvailableGamesForUserId_Success() {
        List<Game> games = Arrays.asList(game);
        Page<Game> expectedGames = new PageImpl<>(games, PageRequest.of(1, 1), 1);
        when(gameRepository.getAvailableGamesForUser(playerXId, 1, 1)).thenReturn(expectedGames);

        Page<Game> result = gameService.getAvailableGamesForUserId(playerXId, 1, 1);

        assertEquals(expectedGames, result);
    }

    @Test
    void getCurrentGamesByUserId_Success() {
        List<Game> games = Arrays.asList(game);
        Page<Game> expectedGames = new PageImpl<>(games, PageRequest.of(1, 1), 1);
        when(gameRepository.getCurrentGamesByUserId(playerXId, 1, 1)).thenReturn(expectedGames);

        Page<Game> result = gameService.getCurrentGamesByUserId(playerXId, 1, 1);

        assertEquals(expectedGames, result);
    }

    @Test
    void getCompletedGamesByUserId_Success() {
        List<Game> games = Arrays.asList(game);
        Page<Game> expectedGames = new PageImpl<>(games, PageRequest.of(1, 1), 1);
        when(gameRepository.getCompletedGamesByUserId(playerXId, 1, 1)).thenReturn(expectedGames);

        Page<Game> result = gameService.getCompletedGamesByUserId(playerXId, 1, 1);

        assertEquals(expectedGames, result);
    }

    @Test
    void getUserStats_Success() {
        UserStats expectedStats = new UserStats(playerXId, 10, 5, 3, 0.5f);
        when(gameRepository.getUserStats(playerXId)).thenReturn(expectedStats);

        UserStats result = gameService.getUserStats(playerXId);

        assertEquals(expectedStats, result);
    }

    @Test
    void getLeaderBoard_Success() {
        List<UserRatio> expectedLeaderboard = Arrays.asList(
                new UserRatio(playerXId, 0.8),
                new UserRatio(playerOId, 0.6)
        );
        when(gameRepository.getLeaderBoard(10)).thenReturn(expectedLeaderboard);

        List<UserRatio> result = gameService.getLeaderBoard(10);

        assertEquals(expectedLeaderboard, result);
    }
}
