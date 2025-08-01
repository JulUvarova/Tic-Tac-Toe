package com.trumpecy.tictactoe.web.controller;

import com.trumpecy.tictactoe.domain.model.game.Board;
import com.trumpecy.tictactoe.domain.model.game.Game;
import com.trumpecy.tictactoe.domain.model.game.GameStatus;
import com.trumpecy.tictactoe.domain.model.stats.UserRatio;
import com.trumpecy.tictactoe.domain.model.stats.UserStats;
import com.trumpecy.tictactoe.domain.service.game.GameService;
import com.trumpecy.tictactoe.exception.EntityNotFoundException;
import com.trumpecy.tictactoe.exception.InvalidGameIdException;
import com.trumpecy.tictactoe.exception.InvalidMoveException;
import com.trumpecy.tictactoe.web.model.PageDto;
import com.trumpecy.tictactoe.web.model.game.GameDtoRequest;
import com.trumpecy.tictactoe.web.model.game.GameDtoResponse;
import com.trumpecy.tictactoe.web.model.game.GameStatusType;
import com.trumpecy.tictactoe.web.model.stats.UserRatioDtoResponse;
import com.trumpecy.tictactoe.web.model.stats.UserStatsDtoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private GameController gameController;

    private Game testGame;
    private UUID testGameId;
    private UUID testUserId;
    private Board testBoard;
    private GameDtoRequest testGameRequest;

    @BeforeEach
    void setUp() {
        testGameId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        testBoard = new Board();
        testBoard.setMatrix(new int[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        });

        testGame = new Game();
        testGame.setId(testGameId);
        testGame.setBoard(testBoard);
        testGame.setPlayerX(testUserId);
        testGame.setPlayerO(UUID.randomUUID());
        testGame.setCurrentPlayer(testUserId);
        testGame.setStatus(GameStatus.IN_PROGRESS);

        testGameRequest = new GameDtoRequest();
        testGameRequest.setBoard(testBoard.getMatrix());

        // Настройка SecurityContext
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        lenient().when(authentication.getPrincipal()).thenReturn(testUserId.toString());
    }

    @Test
    void makeMove_WhenValidMove_ShouldReturnGameResponse() {
        when(gameService.getNextMove(testGameId, testUserId, testBoard.getMatrix())).thenReturn(testGame);

        ResponseEntity<GameDtoResponse> response = gameController.makeMove(testGameId, testGameRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("application/json", response.getHeaders().getFirst("Content-type"));
        verify(gameService).getNextMove(testGameId, testUserId, testBoard.getMatrix());
    }

    @Test
    void makeMove_WhenGameNotFound_ShouldThrowException() {
        when(gameService.getNextMove(testGameId, testUserId, testBoard.getMatrix()))
                .thenThrow(new EntityNotFoundException("Game not found"));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> gameController.makeMove(testGameId, testGameRequest)
        );

        assertEquals("Game not found", exception.getMessage());
        verify(gameService).getNextMove(testGameId, testUserId, testBoard.getMatrix());
    }

    @Test
    void makeMove_WhenInvalidMove_ShouldThrowException() {
        when(gameService.getNextMove(testGameId, testUserId, testBoard.getMatrix()))
                .thenThrow(new InvalidMoveException("Invalid move"));

        InvalidMoveException exception = assertThrows(
                InvalidMoveException.class,
                () -> gameController.makeMove(testGameId, testGameRequest)
        );

        assertEquals("Invalid move", exception.getMessage());
        verify(gameService).getNextMove(testGameId, testUserId, testBoard.getMatrix());
    }

    @Test
    void getGameById_WhenGameExists_ShouldReturnGame() {
        when(gameService.getGameById(testGameId)).thenReturn(testGame);

        ResponseEntity<GameDtoResponse> response = gameController.getGameById(testGameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(gameService).getGameById(testGameId);
    }

    @Test
    void getGameById_WhenGameNotExists_ShouldThrowException() {
        when(gameService.getGameById(testGameId)).thenThrow(new EntityNotFoundException("Game not found"));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> gameController.getGameById(testGameId)
        );

        assertEquals("Game not found", exception.getMessage());
        verify(gameService).getGameById(testGameId);
    }

    @Test
    void getGamesList_ShouldReturnAvailableGames() {
        int page = 0;
        int size = 5;
        List<Game> games = Arrays.asList(testGame);
        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(page, size), games.size());

        when(gameService.getAvailableGamesForUserId(testUserId, page, size)).thenReturn(gamePage);

        ResponseEntity<PageDto<GameDtoResponse>> response = gameController.getGamesList(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(gameService).getAvailableGamesForUserId(testUserId, page, size);
    }

    @Test
    void getGamesList_WithDefaultSize_ShouldUseDefaultValue() {
        int page = 0;
        int defaultSize = 5;
        List<Game> games = Arrays.asList(testGame);
        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(page, defaultSize), games.size());

        when(gameService.getAvailableGamesForUserId(testUserId, page, defaultSize)).thenReturn(gamePage);

        ResponseEntity<PageDto<GameDtoResponse>> response = gameController.getGamesList(page, defaultSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(gameService).getAvailableGamesForUserId(testUserId, page, defaultSize);
    }

    @Test
    void getGamesList_WithPagination_ShouldReturnCorrectPage() {
        int page = 1;
        int size = 3;
        List<Game> games = Arrays.asList(testGame);
        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(page, size), 10);

        when(gameService.getAvailableGamesForUserId(testUserId, page, size)).thenReturn(gamePage);

        ResponseEntity<PageDto<GameDtoResponse>> response = gameController.getGamesList(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getNumber());
        assertEquals(10, response.getBody().getTotalElements());
        verify(gameService).getAvailableGamesForUserId(testUserId, page, size);
    }

    @Test
    void getGamesList_WithCurrentType_ShouldReturnCurrentGames() {
        UUID targetUserId = UUID.randomUUID();
        int page = 0;
        int size = 5;
        List<Game> games = Arrays.asList(testGame);
        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(page, size), games.size());

        when(gameService.getCurrentGamesByUserId(targetUserId, page, size)).thenReturn(gamePage);

        ResponseEntity<PageDto<GameDtoResponse>> response = gameController.getGamesList(
                GameStatusType.CURRENT, page, size, targetUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(gameService).getCurrentGamesByUserId(targetUserId, page, size);
    }

    @Test
    void getGamesList_WithCompletedType_ShouldReturnCompletedGames() {
        UUID targetUserId = UUID.randomUUID();
        int page = 0;
        int size = 5;
        List<Game> games = Arrays.asList(testGame);
        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(page, size), games.size());

        when(gameService.getCompletedGamesByUserId(targetUserId, page, size)).thenReturn(gamePage);

        ResponseEntity<PageDto<GameDtoResponse>> response = gameController.getGamesList(
                GameStatusType.COMPLETED, page, size, targetUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(gameService).getCompletedGamesByUserId(targetUserId, page, size);
    }

    @Test
    void getGamesList_WithDefaultType_ShouldReturnCurrentGames() {
        UUID targetUserId = UUID.randomUUID();
        int page = 0;
        int size = 5;
        List<Game> games = Arrays.asList(testGame);
        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(page, size), games.size());

        when(gameService.getCurrentGamesByUserId(targetUserId, page, size)).thenReturn(gamePage);

        ResponseEntity<PageDto<GameDtoResponse>> response = gameController.getGamesList(
                GameStatusType.CURRENT, page, size, targetUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(gameService).getCurrentGamesByUserId(targetUserId, page, size);
    }

    @Test
    void getUserStats_ShouldReturnUserStats() {
        UUID targetUserId = UUID.randomUUID();
        UserStats userStats = new UserStats();
        userStats.setWins(5);
        userStats.setLosses(3);
        userStats.setDraws(2);

        when(gameService.getUserStats(targetUserId)).thenReturn(userStats);

        ResponseEntity<UserStatsDtoResponse> response = gameController.getUserStats(targetUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(gameService).getUserStats(targetUserId);
    }

    @Test
    void joinGame_WhenValidJoin_ShouldReturnGame() {
        when(gameService.joinGame(testGameId, testUserId)).thenReturn(testGame);

        ResponseEntity<GameDtoResponse> response = gameController.joinGame(testGameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(gameService).joinGame(testGameId, testUserId);
    }

    @Test
    void joinGame_WhenGameNotFound_ShouldThrowException() {
        when(gameService.joinGame(testGameId, testUserId))
                .thenThrow(new EntityNotFoundException("Game not found"));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> gameController.joinGame(testGameId)
        );

        assertEquals("Game not found", exception.getMessage());
        verify(gameService).joinGame(testGameId, testUserId);
    }

    @Test
    void joinGame_WhenInvalidJoin_ShouldThrowException() {
        when(gameService.joinGame(testGameId, testUserId))
                .thenThrow(new InvalidGameIdException("Cannot join this game"));

        InvalidGameIdException exception = assertThrows(
                InvalidGameIdException.class,
                () -> gameController.joinGame(testGameId)
        );

        assertEquals("Cannot join this game", exception.getMessage());
        verify(gameService).joinGame(testGameId, testUserId);
    }

    @Test
    void getLeaderBoard_WithDefaultLimit_ShouldReturnLeaderboard() {
        int defaultLimit = 5;
        List<UserRatio> ratios = Arrays.asList(
                new UserRatio(UUID.randomUUID(), "user1", 0.8),
                new UserRatio(UUID.randomUUID(), "user2", 0.6)
        );

        when(gameService.getLeaderBoard(defaultLimit)).thenReturn(ratios);

        ResponseEntity<List<UserRatioDtoResponse>> response = gameController.getLeaderBoard(defaultLimit);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("application/json", response.getHeaders().getFirst("Content-Type"));
        verify(gameService).getLeaderBoard(defaultLimit);
    }

    @Test
    void getLeaderBoard_WithCustomLimit_ShouldReturnLeaderboard() {
        int customLimit = 10;
        List<UserRatio> ratios = Arrays.asList(
                new UserRatio(UUID.randomUUID(), "user1", 0.8),
                new UserRatio(UUID.randomUUID(), "user2", 0.6),
                new UserRatio(UUID.randomUUID(), "user3", 0.4)
        );

        when(gameService.getLeaderBoard(customLimit)).thenReturn(ratios);

        ResponseEntity<List<UserRatioDtoResponse>> response = gameController.getLeaderBoard(customLimit);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(gameService).getLeaderBoard(customLimit);
    }

    @Test
    void getLeaderBoard_WithEmptyLeaderboard_ShouldReturnEmptyList() {
        int limit = 5;
        List<UserRatio> ratios = Arrays.asList();

        when(gameService.getLeaderBoard(limit)).thenReturn(ratios);

        ResponseEntity<List<UserRatioDtoResponse>> response = gameController.getLeaderBoard(limit);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(gameService).getLeaderBoard(limit);
    }
}