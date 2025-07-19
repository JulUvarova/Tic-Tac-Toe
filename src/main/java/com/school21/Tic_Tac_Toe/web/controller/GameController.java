package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.service.game.GameService;
import com.school21.Tic_Tac_Toe.exception.InvalidGameIdException;
import com.school21.Tic_Tac_Toe.exception.InvalidMoveException;
import com.school21.Tic_Tac_Toe.web.mapper.GameWebMapper;
import com.school21.Tic_Tac_Toe.web.model.GameDto;
import com.school21.Tic_Tac_Toe.web.model.OpponentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    public ResponseEntity<GameDto> makeMove(@PathVariable UUID gameId,
                                            @RequestBody GameDto moveRequest) {
        UUID userId = extractUserId(SecurityContextHolder.getContext().getAuthentication());
        log.info("User {} is moving in game {} ...", userId, gameId);

        Game userMove = GameWebMapper.toGameModel(moveRequest);
        // валидация
        if (!gameId.equals(userMove.getId())) {
            throw new InvalidGameIdException(
                    String.format("Invalid matching game id: request %s, response %s", gameId, userMove.getId()));
        }
        if (gameService.isGameOver(gameId)) {
            throw new InvalidMoveException(
                    String.format("Game %s ended", gameId));
        }
        if (!gameService.getGameById(gameId).getCurrentPlayer().equals(userId)) {
            throw new InvalidMoveException("Invalid user's queue");
        }
        if (!gameService.validateUserBoard(gameId, userMove.getBoard())) {
            throw new InvalidMoveException(
                    String.format("Invalid user's move in game %s", gameId));
        }
        // обработка хода
        Game gameResponse = gameService.getNextMove(gameId, userMove.getBoard());
        log.info("Successful moves in game {}", gameId);
        return ResponseEntity.ok()
                .header("Content-type", "application/json")
                .body(GameWebMapper.toGameDto(gameResponse));
    }

    @Operation(summary = "Create new game",
            description = "Request param sets opponent's type: COMPUTER or USER")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successful creating")})
    @PostMapping
    public ResponseEntity<GameDto> createNewGame(@RequestParam(defaultValue = "COMPUTER") OpponentType opponent) {
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
                .body(GameWebMapper.toGameDto(newGameModel));
    }

    @Operation(summary = "Get game by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GameDto> getGameById(@PathVariable UUID id) {
        log.info("Finding game {}...", id);
        Game gameModel = gameService.getGameById(id);

        log.info("Game {} was found", id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(GameWebMapper.toGameDto(gameModel));
    }

    @Operation(summary = "Get list of available games",
            description = "Get games with status WAITING and one of player isn't requester")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful")})
    @GetMapping()
    public ResponseEntity<List<GameDto>> getAvailableGames() {
        log.info("Getting available games...");
        UUID userId = extractUserId(SecurityContextHolder.getContext().getAuthentication());
        List<GameDto> games = gameService.getAvailableGames(userId).stream().map(GameWebMapper::toGameDto).toList();
        log.info("Got {} games", games.size());
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(games);
    }

    @Operation(summary = "Allow user to join game",
            description = "Game must be WAITING and new user isn't equal second user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "404", description = "Game not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/{gameId}/join")
    public ResponseEntity<GameDto> joinGame(@PathVariable UUID gameId) {
        UUID userId = extractUserId(SecurityContextHolder.getContext().getAuthentication());
        log.info("User {} is joining game {}...", userId, gameId);
        if (gameService.isGameOver(gameId)) {
            throw new InvalidMoveException(
                    String.format("Game %s ended", gameId));
        }
        Game game = gameService.joinGame(userId, gameId);
        log.info("User {} joined game {}", userId, gameId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(GameWebMapper.toGameDto(game));
    }

    private UUID extractUserId(Authentication authentication) {
        return (UUID) authentication.getPrincipal();
    }
}
