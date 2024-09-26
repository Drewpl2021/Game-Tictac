package org.example.tictactoe.controller;

import org.example.tictactoe.entity.Resultado;
import org.example.tictactoe.repository.ResultadoRepository;
import org.example.tictactoe.service.ResultadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/resultados")
public class ResultadoController {

    @Autowired
    private ResultadoService resultadoService;

    // Obtener todos los resultados
    @GetMapping
    public List<Resultado> listarResultados() {
        return resultadoService.listarResultados();
    }

    // Obtener un resultado por ID
    @GetMapping("/{id}")
    public ResponseEntity<Resultado> obtenerResultadoPorId(@PathVariable Long id) {
        Optional<Resultado> resultado = resultadoService.obtenerResultadoPorId(id);
        return resultado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo resultado
    @PostMapping
    public Resultado guardarResultado(@RequestBody Resultado resultado) {
        return resultadoService.guardarResultado(resultado);
    }

    // Actualizar un resultado existente
    @PutMapping("/{id}")
    public ResponseEntity<Resultado> actualizarResultado(@PathVariable Long id, @RequestBody Resultado resultado) {
        try {
            Resultado resultadoActualizado = resultadoService.actualizarResultado(id, resultado);
            return ResponseEntity.ok(resultadoActualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un resultado por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResultado(@PathVariable Long id) {
        resultadoService.eliminarResultado(id);
        return ResponseEntity.ok().build();
    }
}