package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import com.school21.Tic_Tac_Toe.domain.service.GameService;
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
    public ResponseEntity<Object> makeMove(@PathVariable UUID id,
                                           @RequestBody GameDto moveRequest) {
        log.info("User is moving in game {} ...", id);
        GameModel userMove = GameWebMapper.toGameModel(moveRequest);
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
        GameModel gameResponse = gameService.getNextMove(id, userMove.getBoard());
        log.info("Successful moves in game {}", id);
        return ResponseEntity.ok()
                .header("Content-type", "application/json")
                .body(GameWebMapper.toGameDto(gameResponse));
    }

    @Operation(summary = "Create new game")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creating"),
    })
    public ResponseEntity<Object> createNewGame() {
        log.info("User is creating new game...");
        GameModel newGameModel = gameService.createNewGame();
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
}
