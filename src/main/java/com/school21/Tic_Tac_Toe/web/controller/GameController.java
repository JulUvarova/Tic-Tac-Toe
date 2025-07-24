package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.service.game.GameService;
import com.school21.Tic_Tac_Toe.exception.InvalidGameIdException;
import com.school21.Tic_Tac_Toe.exception.InvalidMoveException;
import com.school21.Tic_Tac_Toe.web.mapper.GameWebMapper;
import com.school21.Tic_Tac_Toe.web.mapper.StatsWebMapper;
import com.school21.Tic_Tac_Toe.web.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @Operation(summary = "Make move and get opponent's move ",
            description = "Receive the current game with an updated board from the user and returns the current game with the updated board for the computer's turn. \n" +
                    "If an invalid current game or updated board is sent, the method should return an error with a description.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful move"),
            @ApiResponse(responseCode = "404", description = "Game not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/{gameId}")
    public ResponseEntity<GameDtoResponse> makeMove(@PathVariable UUID gameId,
                                                    @RequestBody @Valid GameDtoRequest userMoveRequest) {
        UUID userId = extractUserId(SecurityContextHolder.getContext().getAuthentication());
        log.info("User {} is moving in game {} ...", userId, gameId);

        // валидация
        if (!gameId.equals(userMoveRequest.getId())) {
            throw new InvalidGameIdException(
                    String.format("Invalid matching game id: request %s, response %s", gameId, userMoveRequest.getId()));
        }
        if (gameService.isGameOver(gameId)) {
            throw new InvalidMoveException(
                    String.format("Game %s ended", gameId));
        }
        if (!gameService.validateUserBoard(userId, gameId, userMoveRequest.getBoard())) {
            throw new InvalidMoveException(
                    String.format("Invalid user's move in game %s", gameId));
        }
        // обработка хода
        Game gameResponse = gameService.getNextMove(gameId, userMoveRequest.getBoard());
        log.info("Successful moves in game {}", gameResponse);
        return ResponseEntity.ok()
                .header("Content-type", "application/json")
                .body(GameWebMapper.toGameResponseDto(gameResponse));
    }

    @Operation(summary = "Create new game",
            description = "Request param sets opponent's type: COMPUTER or USER")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successful creating")})
    @PostMapping
    public ResponseEntity<GameDtoResponse> createNewGame(@RequestParam(defaultValue = "COMPUTER") OpponentType opponent) {
        log.info("User is creating new game with {}...", opponent.name());

        UUID userId = extractUserId(SecurityContextHolder.getContext().getAuthentication());

        Game newGameModel = gameService.createNewGame(userId, opponent);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{gameId}")
                .buildAndExpand(newGameModel.getId())
                .toUri();
        log.info("Created new game {}", newGameModel.getId());
        return ResponseEntity.created(location)
                .header("Content-Type", "application/json")
                .body(GameWebMapper.toGameResponseDto(newGameModel));
    }

    @Operation(summary = "Get game by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GameDtoResponse> getGameById(@PathVariable UUID id) {
        log.info("Finding game {}...", id);
        Game gameModel = gameService.getGameById(id);

        log.info("Game {} was found", id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(GameWebMapper.toGameResponseDto(gameModel));
    }

    @Operation(summary = "Get list of available games",
            description = "Get games with status WAITING and one of player isn't requester")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful")})
    @GetMapping()
    public ResponseEntity<List<GameDtoResponse>> getGamesList() {
        log.info("Getting available games...");
        UUID userId = extractUserId(SecurityContextHolder.getContext().getAuthentication());
        List<GameDtoResponse> games = gameService.getAvailableGamesforUserId(userId)
                .stream()
                .map(GameWebMapper::toGameResponseDto)
                .toList();
        log.info("Got {} games", games.size());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(games);
    }

    @Operation(summary = "Get user's games",
            description = "Get CURRENT (with status IN_PROGRESS, WAITING) or COMPLETED (with status O_WIN, X_WIN or DRAW) games, where user is player")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful")})
    @GetMapping("/player/{userId}")
    public ResponseEntity<List<GameDtoResponse>> getGamesList(@RequestParam(defaultValue = "CURRENT") GameStatusType type, @PathVariable UUID userId) {
        log.info("Getting {} games...", type);
        List<GameDtoResponse> games = findGames(type, userId);
        log.info("Got {} {} games", games.size(), type);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(games);
    }

    @Operation(summary = "Get user stats",
            description = "Get amount of user's wins, losses, draw and win ratio")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful")})
    @GetMapping("/player/{userId}/stats")
    public ResponseEntity<UserStatsDtoResponse> getUserStats(@PathVariable UUID userId) {
        log.info("Getting stats for user {}...", userId);
        UserStatsDtoResponse stats = StatsWebMapper.toStatsDto(gameService.getUserStats(userId));
        log.info("Got stats: {}", stats);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(stats);
    }

    @Operation(summary = "Allow user to join game",
            description = "Game must be WAITING and new user isn't equal second user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Game not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/{gameId}/join")
    public ResponseEntity<GameDtoResponse> joinGame(@PathVariable UUID gameId) {
        UUID userId = extractUserId(SecurityContextHolder.getContext().getAuthentication());
        log.info("User {} is joining game {}...", userId, gameId);
        if (gameService.isGameOver(gameId)) {
            throw new InvalidMoveException(
                    String.format("Game %s ended", gameId));
        }
        Game game = gameService.joinGame(gameId, userId);
        log.info("User {} joined game {}", userId, gameId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(GameWebMapper.toGameResponseDto(game));
    }

    @Operation(summary = "Get leaderboard",
            description = "Get users list with top users and their win rate")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful")})
    @GetMapping("/stats")
    public ResponseEntity<List<UserRatioDtoResponse>> getLeaderBoard(@RequestParam(defaultValue = "5") int limit) {
        log.info("Getting leader board...");
        List<UserRatioDtoResponse> leaderBoard = gameService.getLeaderBoard(limit)
                .stream()
                .map(StatsWebMapper::toRatioDto)
                .toList();
        log.info("Got leader board {}", leaderBoard);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(leaderBoard);
    }

    private UUID extractUserId(Authentication authentication) {
        return (UUID) authentication.getPrincipal();
    }

    private List<GameDtoResponse> findGames(GameStatusType type, UUID userId) {
        List<Game> games = List.of();
        switch (type) {
            case CURRENT -> games = gameService.getCurrentGamesByUserId(userId);
            case COMPLETED -> games = gameService.getCompletedGamesByUserId(userId);
        }

        return games.stream().map(GameWebMapper::toGameResponseDto).toList();
    }
}
