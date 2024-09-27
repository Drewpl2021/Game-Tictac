package org.example.tictactoe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String playerX; // Nombre del jugador X
    private String playerO; // Nombre del jugador O
    private String board; // Estado del tablero (XOXOXO___)
    private boolean isFinished; // Indica si la ronda ha terminado
    private String winner; // Quién ganó esta ronda (playerX, playerO, o "EMPATE")
    @Enumerated(EnumType.STRING)
    private GameStatus status; // Estado de la ronda: JUGANDO, GANADO, EMPATE
    public enum GameStatus {
        JUGANDO,
        EMPATE,
        GANADO,
        ANULADO
    }
}
