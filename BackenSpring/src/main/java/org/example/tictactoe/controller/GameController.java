package org.example.tictactoe.controller;

import org.example.tictactoe.entity.Game;
import org.example.tictactoe.repository.GameRepository;
import org.example.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;

    // Crear un nuevo juego
    @PostMapping
    public Game createNewGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    // Obtener un juego por ID
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    // Realizar un movimiento en el juego
    @PutMapping("/{id}/move")
    public Game makeMove(@PathVariable Long id, @RequestParam int position, @RequestParam String player) {
        return gameService.makeMove(id, position, player);
    }

    // Eliminar un juego por ID
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }

    // Método para actualizar el ganador


    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        game.setStatus(Game.GameStatus.JUGANDO);  // Usar el enum en lugar de un String
        Game newGame = gameService.createGame(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGame);
    }

    @PutMapping("/{id}/winner")
    public ResponseEntity<Game> updateWinner(
            @PathVariable Long id,
            @RequestParam String winner,
            @RequestParam(required = false, defaultValue = "false") boolean isCancelled) {
        try {
            Game updatedGame = gameService.updateWinner(id, winner, isCancelled);
            return ResponseEntity.ok(updatedGame);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}

