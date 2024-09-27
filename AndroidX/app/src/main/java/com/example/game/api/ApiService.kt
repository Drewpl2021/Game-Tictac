package com.example.game.api

import com.example.game.model.Game
import com.example.game.model.Match
import com.example.game.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Método para crear un nuevo juego en el backend
    @POST("api/games")
    fun createGame(@Body game: Game): Call<Game>

    @PATCH("api/games/{id}")
    fun updateWinner(@Path("id") gameId: Long, @Body updatedGame: Map<String, String>): Call<Game>

    @GET("users")
    fun getUsers(): Call<List<User>>

    // Método para crear un nuevo match en el backend
    @POST("matches/create")
    fun createMatch(
        @Query("playerX") playerX: String,
        @Query("playerO") playerO: String,
        @Query("totalRounds") totalRounds: Int
    ): Call<Match>

    // Método para obtener un match por su ID
    @GET("matches/{id}")
    fun getMatchById(@Path("id") matchId: Long): Call<Match>



    // Método para obtener todos los matches de un jugador
    @GET("matches/player/{playerName}")
    fun getMatchesByPlayer(@Path("playerName") playerName: String): Call<List<Match>>

    @PUT("api/games/{id}/winner")
    fun updateWinner(
        @Path("id") gameId: Long,
        @Query("winner") winner: String,
        @Query("isCancelled") isCancelled: Boolean = false
    ): Call<Game>
    // Método para actualizar el ganador de una ronda en un match
    @PUT("matches/{id}/winner")
    fun updateMatchWinner(
        @Path("id") matchId: Long,
        @Query("winner") winner: String,
        @Query("isCancelled") isCancelled: Boolean = false
    ): Call<Match>
}
