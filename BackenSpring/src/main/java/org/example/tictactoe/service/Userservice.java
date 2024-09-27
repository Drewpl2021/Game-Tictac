package org.example.tictactoe.service;

import org.example.tictactoe.entity.User;
import org.example.tictactoe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Userservice {
    @Autowired
    private UserRepository userRepository;

    // Crear o actualizar un usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener un usuario por ID
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Eliminar un usuario
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }
}
