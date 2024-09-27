package org.example.tictactoe.service;

import org.example.tictactoe.entity.Game;
import org.example.tictactoe.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    public Game createGame(Game game) {
        game.setBoard("_________");
        game.setFinished(false);
        return gameRepository.save(game);
    }


    public Game getGameById(Long id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.orElseThrow(() -> new RuntimeException("Game no encontrado"));
    }

    public void deleteGame(Long id) {
        Game game = getGameById(id);
        gameRepository.delete(game);
    }

    // Actualizar el tablero y hacer un movimiento
    public Game makeMove(Long id, int position, String player) {
        Game game = getGameById(id);

        if (game.isFinished()) {
            throw new RuntimeException("Game ya esta terminado");
        }

        char[] boardArray = game.getBoard().toCharArray();

        // Verificar si la posición está vacía
        if (boardArray[position] == '_') {
            boardArray[position] = player.charAt(0);
            game.setBoard(String.valueOf(boardArray));
            // Aquí puedes añadir lógica para verificar si alguien ha ganado
        } else {
            throw new RuntimeException("Position already taken");
        }

        return gameRepository.save(game);
    }

    // Verificar si hay un ganador (a futuro)
    public String checkWinner(Game game) {
        // Lógica para verificar si hay un ganador
        // Esto puede ser tan simple como revisar las combinaciones ganadoras
        return "no winner";
    }


    // Método para actualizar el ganador del juego
    public Game updateWinner(Long gameId, String winner, boolean isCancelled) throws Exception {
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();

            if (isCancelled) {
                game.setStatus(Game.GameStatus.ANULADO);  // Cambiar el estado a ANULADO
                game.setWinner(null);  // Si es anulado, no debería haber ganador
            } else {
                game.setWinner(winner);
                game.setStatus(Game.GameStatus.GANADO);  // Cambiar el estado a GANADO
            }

            return gameRepository.save(game);
        } else {
            throw new Exception("Juego no encontrado");
        }
    }



    public Game updateGame(Game game) {
        return gameRepository.save(game);
    }
}
