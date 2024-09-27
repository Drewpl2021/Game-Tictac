package org.example.tictactoe.service;

import org.example.tictactoe.entity.Matchs;
import org.example.tictactoe.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    // Crear un nuevo match (mejor de 3, 5, etc.)
    public Matchs createMatch(String playerX, String playerO, int totalRounds) {
        Matchs match = new Matchs();
        match.setPlayerX(playerX);
        match.setPlayerO(playerO);
        match.setTotalRounds(totalRounds);
        match.setRoundsWonByPlayerX(0);
        match.setRoundsWonByPlayerO(0);
        match.setStatus(Matchs.MatchStatus.JUGANDO);
        return matchRepository.save(match);
    }

    // Obtener un match por su ID
    public Matchs getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match no encontrado"));
    }

    // Actualizar el puntaje de las rondas ganadas por cada jugador y determinar si el match ha finalizado
    public Matchs updateMatchWinner(Long matchId, String winner, boolean isCancelled) throws Exception {
        Matchs match = getMatchById(matchId);

        // Verificar si el partido ha sido anulado
        if (isCancelled) {
            match.setMatchWinner(null);  // No hay ganador si el match es anulado
            match.setStatus(Matchs.MatchStatus.ANULADO);  // Cambiar el estado a "ANULADO"
            match.setFinished(true);  // Marcamos el partido como terminado
            return matchRepository.save(match);
        }

        // Actualizar las rondas ganadas según el ganador de la ronda
        if (winner.equals(match.getPlayerX())) {
            match.setRoundsWonByPlayerX(match.getRoundsWonByPlayerX() + 1);
        } else if (winner.equals(match.getPlayerO())) {
            match.setRoundsWonByPlayerO(match.getRoundsWonByPlayerO() + 1);
        }

        // Verificar si alguien ha ganado el match (mayoría de rondas ganadas)
        if (match.getRoundsWonByPlayerX() > match.getTotalRounds() / 2) {
            match.setMatchWinner(match.getPlayerX());
            match.setStatus(Matchs.MatchStatus.FINALIZADO);
            match.setFinished(true);  // El match ha terminado
        } else if (match.getRoundsWonByPlayerO() > match.getTotalRounds() / 2) {
            match.setMatchWinner(match.getPlayerO());
            match.setStatus(Matchs.MatchStatus.FINALIZADO);
            match.setFinished(true);  // El match ha terminado
        }

        return matchRepository.save(match);
    }

    // Eliminar un match por su ID
    public void deleteMatch(Long id) {
        Matchs match = getMatchById(id);
        matchRepository.delete(match);
    }

    // Obtener todos los matches de un jugador+
    public List<Matchs> getMatchesByPlayer(String playerName) {
        return matchRepository.findByPlayerXOrPlayerO(playerName, playerName);
    }

    // Otros métodos pueden agregarse aquí si es necesario
}
