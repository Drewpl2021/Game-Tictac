package com.example.game.model

data class Match (
    val id: Long,
    val playerX: String, // Nombre del jugador X
    val playerO: String, // Nombre del jugador O
    val board: String, // Estado del tablero (XOXOXO___)
    val isFinished: Boolean, // Indica si la ronda ha terminado
    val totalRounds: Int, // Total de rondas en el match (por ejemplo, 3 o 5)
    val roundsWonByPlayerX: Int, // Número de rondas ganadas por playerX
    val roundsWonByPlayerO: Int, // Número de rondas ganadas por playerO
    var matchWinner: String,// Quién ganó el match completo (playerX o playerO)
    val status: String
    )