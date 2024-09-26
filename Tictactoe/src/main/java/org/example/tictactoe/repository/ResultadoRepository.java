package org.example.tictactoe.repository;

import org.example.tictactoe.entity.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    // Aquí puedes definir consultas personalizadas si es necesario
}

