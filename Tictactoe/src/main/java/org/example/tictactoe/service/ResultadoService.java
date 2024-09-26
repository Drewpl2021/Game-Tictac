package org.example.tictactoe.service;

import org.example.tictactoe.entity.Resultado;
import org.example.tictactoe.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultadoService {

    @Autowired
    private ResultadoRepository resultadoRepository;

    public List<Resultado> listarResultados() {
        return resultadoRepository.findAll();
    }

    public Optional<Resultado> obtenerResultadoPorId(Long id) {
        return resultadoRepository.findById(id);
    }

    public Resultado guardarResultado(Resultado resultado) {
        return resultadoRepository.save(resultado);
    }

    public Resultado actualizarResultado(Long id, Resultado resultadoActualizado) throws Exception {
        Optional<Resultado> optionalResultado = resultadoRepository.findById(id);
        if (optionalResultado.isPresent()) {
            Resultado resultadoExistente = optionalResultado.get();
            resultadoExistente.setNombrePartida(resultadoActualizado.getNombrePartida());
            resultadoExistente.setNombreJugador1(resultadoActualizado.getNombreJugador1());
            resultadoExistente.setNombreJugador2(resultadoActualizado.getNombreJugador2());
            resultadoExistente.setGanador(resultadoActualizado.getGanador());
            resultadoExistente.setPunto(resultadoActualizado.getPunto());
            resultadoExistente.setEstado(resultadoActualizado.getEstado());
            return resultadoRepository.save(resultadoExistente);
        } else {
            throw new Exception("Resultado no encontrado");
        }
    }

    public void eliminarResultado(Long id) {
        resultadoRepository.deleteById(id);
    }
}