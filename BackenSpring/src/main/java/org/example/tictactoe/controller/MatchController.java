package org.example.tictactoe.controller;

import org.example.tictactoe.entity.Matchs;
import org.example.tictactoe.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")  // Todas las rutas para el match estarán bajo /matches
public class MatchController {

    @Autowired
    private MatchService matchService;

    // Crear un nuevo Match
    @PostMapping("/create")
    public ResponseEntity<Matchs> createMatch(
            @RequestParam String playerX,
            @RequestParam String playerO,
            @RequestParam int totalRounds) {
        Matchs match = matchService.createMatch(playerX, playerO, totalRounds);
        return ResponseEntity.status(HttpStatus.CREATED).body(match);
    }

    // Obtener un Match por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Matchs> getMatch(@PathVariable Long id) {
        Matchs match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }

    // Actualizar el ganador de una ronda en un Match
    @PutMapping("/{id}/winner")
    public ResponseEntity<Matchs> updateMatchWinner(
            @PathVariable Long id,
            @RequestParam String winner,
            @RequestParam(required = false, defaultValue = "false") boolean isCancelled) throws Exception {
        // Llamamos al servicio, pasando el parámetro isCancelled
        Matchs updatedMatch = matchService.updateMatchWinner(id, winner, isCancelled);
        return ResponseEntity.ok(updatedMatch);
    }

    // Eliminar un Match por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener todos los Matches jugados por un jugador
    @GetMapping("/player/{playerName}")
    public ResponseEntity<List<Matchs>> getMatchesByPlayer(@PathVariable String playerName) {
        List<Matchs> matches = matchService.getMatchesByPlayer(playerName);
        return ResponseEntity.ok(matches);
    }


}
