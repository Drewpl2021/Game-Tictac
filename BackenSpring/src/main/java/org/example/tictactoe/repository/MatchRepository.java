package org.example.tictactoe.repository;

import org.example.tictactoe.entity.Matchs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Matchs, Long> {
    // Métodos personalizados si es necesario
    List<Matchs> findByPlayerXOrPlayerO(String playerX, String playerO); // Buscar partidos por jugador
}
