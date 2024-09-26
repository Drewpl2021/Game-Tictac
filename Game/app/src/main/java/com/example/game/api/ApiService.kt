package com.example.game.api

import com.example.game.model.Resultado
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Crear una nueva partida
    @POST("api/resultados")
    fun crearPartida(@Body nuevaPartida: Resultado): Call<Resultado>

    // Actualizar el ganador y los puntos al terminar el juego
    @PATCH("api/resultados/{id}")
    fun actualizarResultado(
        @Path("id") id: Long,
        @Body resultadoActualizado: Resultado
    ): Call<Resultado>

    // Obtener la lista de resultados de las partidas
    @GET("api/resultados")
    fun obtenerResultados(): Call<List<Resultado>>
}