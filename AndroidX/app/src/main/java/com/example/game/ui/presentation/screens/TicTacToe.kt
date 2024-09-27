package com.example.game.ui.presentation.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.api.ApiService
import com.example.game.api.RetrofitClient
import com.example.game.model.Game
import com.example.game.ui.theme.GameTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import com.example.game.model.Match

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var gameCreated by remember { mutableStateOf<Game?>(null) }
            var matchCreated by remember { mutableStateOf<Match?>(null) } // Para manejar Match
            var gameStarted by remember { mutableStateOf(false) } // Controla si el juego ha comenzado
            var matchStarted by remember { mutableStateOf(false) } // Controla si el Match ha comenzado
            var playerX by remember { mutableStateOf("") }
            var playerO by remember { mutableStateOf("") }

            if (gameStarted) {
                TicTacToeBoard(
                    gameCreated!!, playerX, playerO,
                    onExit = {
                        gameStarted = false
                        playerX = ""
                        playerO = ""
                    }
                )
            } else if (matchStarted) {
                // Aquí mostrarías la lógica del match
                MatchBoard(
                    matchCreated!!, playerX, playerO,
                    onExit = {
                        matchStarted = false
                        playerX = ""
                        playerO = ""
                    }
                )
            } else {
                GameModeSelectionScreen(
                    onGameSelected = { game, pX, pO ->
                        gameCreated = game
                        playerX = pX
                        playerO = pO
                        gameStarted = true
                    },
                    onMatchSelected = { match, pX, pO ->
                        matchCreated = match
                        playerX = pX
                        playerO = pO
                        matchStarted = true
                    }
                )
            }
        }
    }
}

@Composable
fun MatchBoard(match: Match, playerX: String, playerO: String, onExit: () -> Unit) {
    var currentRound by remember { mutableStateOf(1) }  // Ronda actual
    var currentPlayer by remember { mutableStateOf("X") }
    var board by remember { mutableStateOf(CharArray(9) { '_' }) }
    var result by remember { mutableStateOf<String?>(null) }
    var matchFinished by remember { mutableStateOf(false) }
    val moveHistory = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar la ronda actual
        Text(
            text = "Ronda $currentRound de ${match.totalRounds}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mostrar nombre del jugador actual o resultado final
        Text(
            text = result?.let {
                if (it == "EMPATE") "Empate en la Ronda $currentRound" else "Ganador: $it en la Ronda $currentRound"
            } ?: "Turno de ${if (currentPlayer == "X") playerX else playerO}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            color = MaterialTheme.colorScheme.primary
        )

        // Tablero de juego para la ronda actual
        Board(board = board, onCellClick = { index ->
            if (board[index] == '_' && !matchFinished) {
                val move = "Jugador ${if (currentPlayer == "X") playerX else playerO} marcó en la posición ${index + 1}"
                moveHistory.add(move)
                board[index] = currentPlayer.single()
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                checkWinnerForMatch(board, match.id, playerX, playerO) { roundResult ->
                    if (roundResult != null) {
                        result = roundResult
                        if (currentRound < match.totalRounds) {
                            // Si quedan más rondas, avanzamos
                            currentRound++
                            board = CharArray(9) { '_' }  // Reiniciar el tablero para la nueva ronda
                            result = null
                        } else {
                            // Si no quedan más rondas, terminamos el Match
                            matchFinished = true
                        }
                    }
                }
            }
        })

        // Mostrar el historial de movimientos
        Text("Historial de Movimientos", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(moveHistory) { move ->
                Text(text = move, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Botón para salir
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                updateMatchWinner(match.id, "", isCancelled = true)  // Aquí anulas el match
                onExit()  // Aquí vuelves a la pantalla de selección
            },
            modifier = Modifier
                .padding(16.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Salir", fontSize = 18.sp, color = MaterialTheme.colorScheme.onError)
        }
    }
}
fun checkWinnerForMatch(
    board: CharArray,
    matchId: Long,
    playerX: String,
    playerO: String,
    onResult: (String?) -> Unit // Usamos onResult para manejar el resultado de cada ronda
) {
    val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Filas
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columnas
        listOf(0, 4, 8), listOf(2, 4, 6)  // Diagonales
    )

    for (pattern in winPatterns) {
        val (a, b, c) = pattern
        if (board[a] != '_' && board[a] == board[b] && board[b] == board[c]) {
            val winner = if (board[a] == 'X') playerX else playerO
            updateMatchWinner(matchId, winner) // Guardar el ganador en la base de datos
            onResult(winner) // Retornar el ganador de la ronda
            return
        }
    }

    if (board.none { it == '_' }) {
        updateMatchWinner(matchId, "EMPATE") // Guardar "EMPATE" en la base de datos
        onResult("EMPATE") // Retornar que es empate
    } else {
        onResult(null) // Si no hay ganador ni empate, continúa el juego
    }
}
@Composable
fun GameModeSelectionScreen(
    onGameSelected: (Game, String, String) -> Unit,
    onMatchSelected: (Match, String, String) -> Unit
) {
    var playerX by remember { mutableStateOf("") }
    var playerO by remember { mutableStateOf("") }
    var totalRounds by remember { mutableStateOf(3) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Inputs para los nombres de los jugadores
        OutlinedTextField(
            value = playerX,
            onValueChange = { playerX = it },
            label = { Text("Jugador 1") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = playerO,
            onValueChange = { playerO = it },
            label = { Text("Jugador 2") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para crear una partida única
        Button(
            onClick = {
                if (playerX.isNotBlank() && playerO.isNotBlank()) {
                    createNewGame(playerX, playerO) { newGame, error ->
                        if (error == null) {
                            onGameSelected(newGame!!, playerX, playerO)
                        } else {
                            errorMessage = error
                        }
                    }
                } else {
                    errorMessage = "Por favor ingresa los nombres de ambos jugadores"
                }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Iniciar Partida Única")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para definir el número de rondas
        OutlinedTextField(
            value = totalRounds.toString(),
            onValueChange = { totalRounds = it.toIntOrNull() ?: 3 },
            label = { Text("Total de Rondas (Mejor de 3, 5, etc.)") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        // Botón para crear un Match
        Button(
            onClick = {
                if (playerX.isNotBlank() && playerO.isNotBlank()) {
                    createNewMatch(playerX, playerO, totalRounds) { newMatch, error ->
                        if (error == null) {
                            onMatchSelected(newMatch!!, playerX, playerO)
                        } else {
                            errorMessage = error
                        }
                    }
                } else {
                    errorMessage = "Por favor ingresa los nombres de ambos jugadores"
                }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Iniciar Match (Múltiples Rondas)")
        }

        // Mostrar error si existe
        errorMessage?.let {
            Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}

fun updateMatchWinner(matchId: Long, winner: String) {
    RetrofitClient.instance.updateMatchWinner(matchId, winner)
        .enqueue(object : Callback<Match> {
            override fun onResponse(call: Call<Match>, response: Response<Match>) {
                if (response.isSuccessful) {
                    Log.d("Match", "Resultado guardado: $winner")
                } else {
                    Log.e("Match", "Error al guardar el resultado: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Match>, t: Throwable) {
                Log.e("Match", "Error de conexión: ${t.message}")
            }
        })
}

// Función para crear un nuevo juego en el backend
fun createNewGame(playerX: String, playerO: String, onGameCreated: (Game?, String?) -> Unit) {
    val newGame = Game(0, playerX, playerO, "___", false, null, "JUGANDO")  // Estado inicial JUGANDO
    RetrofitClient.instance.createGame(newGame)
        .enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) {
                    response.body()?.let { onGameCreated(it, null) }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("Game", "Error al crear el juego: $error")
                    onGameCreated(null, error ?: "Error al crear el juego")
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Log.e("Game", "Error de conexión: ${t.message}")
                onGameCreated(null, t.message ?: "Error de conexión")
            }
        })
}

fun createNewMatch(playerX: String, playerO: String, totalRounds: Int, onMatchCreated: (Match?, String?) -> Unit) {
    RetrofitClient.instance.createMatch(playerX, playerO, totalRounds)
        .enqueue(object : Callback<Match> {
            override fun onResponse(call: Call<Match>, response: Response<Match>) {
                if (response.isSuccessful) {
                    response.body()?.let { onMatchCreated(it, null) }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("Match", "Error al crear el match: $error")
                    onMatchCreated(null, error ?: "Error al crear el match")
                }
            }

            override fun onFailure(call: Call<Match>, t: Throwable) {
                Log.e("Match", "Error de conexión: ${t.message}")
                onMatchCreated(null, t.message ?: "Error de conexión")
            }
        })
}

@Composable
fun TicTacToeBoard(game: Game, playerX: String, playerO: String, onExit: () -> Unit) {
    var board by remember { mutableStateOf(game.board.toCharArray()) }
    var currentPlayer by remember { mutableStateOf("X") }
    var result by remember { mutableStateOf<String?>(null) }
    var gameFinished by remember { mutableStateOf(false) }
    val moveHistory = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar nombre del jugador actual o resultado final
        Text(
            text = result?.let {
                if (it == "EMPATE") "Empate" else "Ganador: $it"
            } ?: "Turno de ${if (currentPlayer == "X") playerX else playerO}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            color = MaterialTheme.colorScheme.primary
        )

        // Tablero de juego
        Board(board = board, onCellClick = { index ->
            if (board[index] == '_' && !gameFinished) {
                val move = "Jugador ${if (currentPlayer == "X") playerX else playerO} marcó en la posición ${index + 1}"
                moveHistory.add(move)
                board[index] = currentPlayer.single()
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                checkWinner(board, game.id, playerX, playerO) { gameResult ->
                    if (gameResult != null) {
                        result = gameResult
                        gameFinished = true
                    }
                }
            }
        })

        // Botón para Reiniciar Juego
        if (gameFinished) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onExit() },  // Llama a la función onExit cuando se haga clic
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Salir", fontSize = 18.sp, color = MaterialTheme.colorScheme.onError)
            }
        }

        // Botón para Salir
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                updateGameWinner(game.id, "", isCancelled = true)  // Aquí anulas el juego
                onExit()  // Aquí vuelves a la pantalla de selección
            },
            modifier = Modifier
                .padding(16.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Anular", fontSize = 18.sp, color = MaterialTheme.colorScheme.onError)
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar historial de movimientos
        Text("Historial de Movimientos", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(moveHistory) { move ->
                Text(text = move, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Mostrar resultado final
        result?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Resultado final: $it",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun Board(board: CharArray, onCellClick: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Centramos las filas
        verticalArrangement = Arrangement.spacedBy(4.dp) // Espaciado entre filas
    ) {
        for (row in 0..2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Espaciado entre columnas
            ) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    BoardCell(value = board[index].toString(), onClick = { onCellClick(index) })
                }
            }
        }
    }
}




@Composable
fun BoardCell(value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(80.dp) // Tamaño de la celda
            .padding(2.dp) // Padding reducido para que haya menos espacio entre celdas
            .clickable(onClick = onClick)
            .shadow(6.dp, RoundedCornerShape(8.dp)), // Sombra más suave y bordes redondeados
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Color de fondo neutro
    ) {
        Box(
            modifier = Modifier
                .background(Color.DarkGray) // Fondo para el modo oscuro
                .border(2.dp, Color.White)  // Borde blanco para contraste en oscuro
                .size(100.dp),
            contentAlignment = Alignment.Center // Centrar el texto dentro de la celda
        ) {
            Text(
                text = if (value == "_") "" else value, // Si es "_", no mostrar nada
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 36.sp, // Tamaño de la letra ajustado a la celda
                    fontWeight = FontWeight.Bold // Letras en negrita para mejor visibilidad
                ),
                color = when (value) {
                    "X" -> MaterialTheme.colorScheme.primary // Color para X
                    "O" -> MaterialTheme.colorScheme.secondary // Color para O
                    else -> MaterialTheme.colorScheme.onSurface // Si no es X ni O
                },
                modifier = Modifier.padding(4.dp), // Añadir un pequeño padding interno
                textAlign = TextAlign.Center // Asegurar que el texto esté centrado
            )
        }
    }
}



fun checkWinner(
    board: CharArray,
    gameId: Long,
    playerX: String,
    playerO: String,
    onResult: (String?) -> Unit // Usamos onResult para manejar ganador o empate
) {
    val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Filas
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columnas
        listOf(0, 4, 8), listOf(2, 4, 6)  // Diagonales
    )

    // Revisar si hay un ganador
    for (pattern in winPatterns) {
        val (a, b, c) = pattern
        if (board[a] != '_' && board[a] == board[b] && board[b] == board[c]) {
            val winner = if (board[a] == 'X') playerX else playerO
            updateGameWinner(gameId, winner) // Guardar el ganador en la base de datos
            onResult(winner) // Retornar el ganador
            return
        }
    }

    // Revisar si hay empate (tablero lleno)
    if (board.none { it == '_' }) {
        updateGameWinner(gameId, "EMPATE") // Guardar "EMPATE" en la base de datos
        onResult("EMPATE") // Retornar que es empate
    } else {
        onResult(null) // Si no hay ganador ni empate, continúa el juego
    }
}


fun updateMatchWinner(matchId: Long, winner: String, isCancelled: Boolean = false) {
    RetrofitClient.instance.updateMatchWinner(matchId, winner, isCancelled)
        .enqueue(object : Callback<Match> {
            override fun onResponse(call: Call<Match>, response: Response<Match>) {
                if (response.isSuccessful) {
                    Log.d("Match", if (isCancelled) "Partido anulado" else "Resultado guardado: $winner")
                } else {
                    Log.e("Match", "Error al guardar el resultado: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Match>, t: Throwable) {
                Log.e("Match", "Error de conexión: ${t.message}")
            }
        })
}


fun updateGameWinner(gameId: Long, winner: String, isCancelled: Boolean = false) {
    RetrofitClient.instance.updateWinner(gameId, winner, isCancelled)
        .enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) {
                    Log.d("Game", if (isCancelled) "Juego anulado" else "Resultado guardado: $winner")
                } else {
                    Log.e("Game", "Error al guardar el resultado: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Log.e("Game", "Error de conexión: ${t.message}")
            }
        })
}



@Preview(showBackground = true)
@Composable
fun TicTacToeGame() {
    var gameCreated by remember { mutableStateOf<Game?>(null) }
    var matchCreated by remember { mutableStateOf<Match?>(null) }  // Para manejar Match
    var gameStarted by remember { mutableStateOf(false) }  // Controla si el juego ha comenzado
    var matchStarted by remember { mutableStateOf(false) }  // Controla si el Match ha comenzado
    var playerX by remember { mutableStateOf("") }
    var playerO by remember { mutableStateOf("") }

    // Pantalla para elegir entre jugar una partida única o un match
    if (gameStarted) {
        TicTacToeBoard(
            game = gameCreated!!,
            playerX = playerX,
            playerO = playerO,
            onExit = {
                // Aquí simplemente volvemos a la pantalla de entrada de jugadores
                gameStarted = false
                playerX = ""
                playerO = ""
            }
        )
    } else if (matchStarted) {
        MatchBoard(
            match = matchCreated!!,
            playerX = playerX,
            playerO = playerO,
            onExit = {
                // Aquí simplemente volvemos a la pantalla de entrada de jugadores
                matchStarted = false
                playerX = ""
                playerO = ""
            }
        )
    } else {
        GameModeSelectionScreen(
            onGameSelected = { game, pX, pO ->
                gameCreated = game
                playerX = pX
                playerO = pO
                gameStarted = true  // Cambia a la pantalla del tablero para una partida única
            },
            onMatchSelected = { match, pX, pO ->
                matchCreated = match
                playerX = pX
                playerO = pO
                matchStarted = true  // Cambia a la pantalla del match
            }
        )
    }
}


