package com.example.game.model

data class Resultado(
    val idResultado: Long? = null,
    val nombrePartida: String,
    val nombreJugador1: String,
    val nombreJugador2: String,
    val ganador: String,
    val punto: Int,
    val estado: String
)