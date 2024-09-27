package org.example.tictactoe.repository;

import org.example.tictactoe.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
}

