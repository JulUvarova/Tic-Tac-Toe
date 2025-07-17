package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.service.game.GameService;
import com.school21.Tic_Tac_Toe.exception.InvalidGameIdException;
import com.school21.Tic_Tac_Toe.exception.InvalidMoveException;
import com.school21.Tic_Tac_Toe.web.mapper.GameWebMapper;
import com.school21.Tic_Tac_Toe.web.model.GameDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
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
    @PostMapping("/{id}")
    public ResponseEntity<GameDto> makeMove(@PathVariable UUID id,
                                            @RequestBody GameDto moveRequest) {
        log.info("User is moving in game {} ...", id);
        Game userMove = GameWebMapper.toGameModel(moveRequest);
        // валидация
        if (!id.equals(userMove.getId())) {
            throw new InvalidGameIdException(
                    String.format("Invalid matching game id: request %s, response %s", id, userMove.getId()));
        }
        if (gameService.isGameOver(id)) {
            throw new InvalidMoveException(
                    String.format("Game %s ended", id));
        }
        if (!gameService.validateUserBoard(id, userMove.getBoard())) {
            throw new InvalidMoveException(
                    String.format("Invalid user's move in game %s", id));
        }
        // обработка хода
        Game gameResponse = gameService.getNextMove(id, userMove.getBoard());
        log.info("Successful moves in game {}", id);
        return ResponseEntity.ok()
                .header("Content-type", "application/json")
                .body(GameWebMapper.toGameDto(gameResponse));
    }

    @Operation(summary = "Create new game")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successful creating")})
    @PostMapping
    public ResponseEntity<GameDto> createNewGame() {
        log.info("User is creating new game...");
        Game newGameModel = gameService.createNewGame();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newGameModel.getId())
                .toUri();
        log.info("Create new game {}", newGameModel.getId());
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

        log.info("Game {} is found", id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(GameWebMapper.toGameDto(gameModel));
    }
}
