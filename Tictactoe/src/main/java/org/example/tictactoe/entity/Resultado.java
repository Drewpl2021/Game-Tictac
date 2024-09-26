package org.example.tictactoe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "resultados")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResultado;

    @Column(name = "nombre_partida", nullable = false, length = 30)
    private String nombrePartida;

    @Column(name = "nombre_jugador1", nullable = false, length = 40)
    private String nombreJugador1;

    @Column(name = "nombre_jugador2", nullable = false, length = 40)
    private String nombreJugador2;

    @Column(name = "ganador", nullable = false, length = 40)
    private String ganador;

    @Column(name = "punto", nullable = false)
    private Integer punto;

    @Column(name = "estado", nullable = false, length = 10)
    private String estado; // Podría ser "En curso", "Finalizado", etc.
}



