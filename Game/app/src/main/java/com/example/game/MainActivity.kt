package com.example.game

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.game.api.RetrofitClient
import com.example.game.model.Resultado
import com.example.game.ui.theme.GameTheme
import retrofit2.Call

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeScreen() // Llamamos a la pantalla principal del juego
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GameTheme {
        Greeting("Android")
    }
}





@Composable
fun TicTacToeScreen() {
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var isGameStarted by remember { mutableStateOf(false) } // Para controlar el inicio del juego
    var partidaId by remember { mutableStateOf<Long?>(null) } // ID de la partida
    var board by remember { mutableStateOf(List(9) { "_" }) }  // Tablero vacío
    var currentPlayer by remember { mutableStateOf("X") }       // Empieza jugador X
    var winner by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("3 en Raya", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de entrada para los nombres de los jugadores
        TextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Nombre Jugador 1") },
            enabled = !isGameStarted, // Deshabilitado si el juego ha comenzado
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = player2Name,
            onValueChange = { player2Name = it },
            label = { Text("Nombre Jugador 2") },
            enabled = !isGameStarted, // Deshabilitado si el juego ha comenzado
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones "Iniciar" y "Anular"
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (player1Name.isNotEmpty() && player2Name.isNotEmpty()) {
                        crearPartida("Partida 1", player1Name, player2Name) { id ->
                            partidaId = id // Guardamos el ID de la partida creada
                            isGameStarted = true // Solo se permite jugar después de iniciar
                        }
                    }
                },
                enabled = !isGameStarted
            ) {
                Text("Iniciar")
            }

            Button(
                onClick = {
                    isGameStarted = false
                    board = List(9) { "_" } // Reinicia el tablero
                    winner = null
                    partidaId = null // Restablecemos el ID de la partida
                }
            ) {
                Text("Anular")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tablero de 3 en raya
        for (i in 0..2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 0..2) {
                    val index = i * 3 + j
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable(enabled = isGameStarted && board[index] == "_") {
                                if (isGameStarted && board[index] == "_") {
                                    board = board.toMutableList().apply {
                                        set(index, currentPlayer)
                                    }
                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    winner = verificarGanador(board)
                                    if (winner != null) {
                                        val puntos = if (winner == "Empate") 5 else 10
                                        actualizarResultado(partidaId!!, winner!!, puntos, "Finalizado")
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[index],
                            style = MaterialTheme.typography.headlineLarge,
                            color = if (board[index] == "X") Color.Red else Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Indicador de turno o ganador
        if (winner != null) {
            Text("Ganador: $winner", style = MaterialTheme.typography.headlineSmall)
        } else {
            Text("Turno de $currentPlayer", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabla de puntajes simulada
        ScoreTable(
            partidaId = 1,
            winner = winner ?: "Empate",
            puntos1 = 10,  // Puedes cambiar los valores reales de puntos
            puntos2 = 5    // Puedes cambiar los valores reales de puntos
        )
    }
}


@Composable
fun ScoreTable(
    partidaId: Int,  // ID de la partida
    winner: String,  // Ganador de la partida
    puntos1: Int,    // Puntos del Jugador 1
    puntos2: Int     // Puntos del Jugador 2
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "TABLA DE PUNTOS",
            style = MaterialTheme.typography.headlineSmall
        )
        Divider(Modifier.padding(8.dp))
        Text(
            text = "Partido $partidaId",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Ganador: $winner",
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "P1: $puntos1",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "P2: $puntos2",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}



fun verificarGanador(board: List<String>): String? {
    val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Filas
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columnas
        listOf(0, 4, 8), listOf(2, 4, 6)  // Diagonales
    )

    for (pattern in winPatterns) {
        val (a, b, c) = pattern
        if (board[a] == board[b] && board[b] == board[c] && board[a] != "_") {
            return board[a] // Devuelve el ganador ("X" o "O")
        }
    }

    if (board.all { it != "_" }) {
        return "Empate"
    }

    return null // No hay ganador aún
}

fun crearPartida(nombrePartida: String, jugador1: String, jugador2: String, onSuccess: (Long) -> Unit) {
    val nuevaPartida = Resultado(
        nombrePartida = nombrePartida,
        nombreJugador1 = jugador1,
        nombreJugador2 = jugador2,
        ganador = "", // No hay ganador aún
        punto = 0,
        estado = "En curso"
    )

    // Llamada al backend para crear la partida
    RetrofitClient.instance.crearPartida(nuevaPartida)
        .enqueue(object : retrofit2.Callback<Resultado> {
            override fun onResponse(call: Call<Resultado>, response: retrofit2.Response<Resultado>) {
                if (response.isSuccessful) {
                    response.body()?.idResultado?.let { id ->
                        onSuccess(id)  // Devuelve el ID de la partida creada
                    }
                } else {
                    Log.e("Retrofit", "Error al crear partida: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Resultado>, t: Throwable) {
                Log.e("Retrofit", "Fallo en la conexión: ${t.message}")
            }
        })
}

fun actualizarResultado(idPartida: Long, ganador: String, puntos: Int, estado: String) {
    val resultadoActualizado = Resultado(
        idResultado = idPartida,
        nombrePartida = "Partida actual",  // Puedes cambiar este valor según corresponda
        nombreJugador1 = "Jugador 1",      // Pasa el nombre real del jugador 1
        nombreJugador2 = "Jugador 2",      // Pasa el nombre real del jugador 2
        ganador = ganador,
        punto = puntos,
        estado = estado
    )

    // Llamada al backend para actualizar el resultado
    RetrofitClient.instance.actualizarResultado(idPartida, resultadoActualizado)
        .enqueue(object : retrofit2.Callback<Resultado> {
            override fun onResponse(call: Call<Resultado>, response: retrofit2.Response<Resultado>) {
                if (response.isSuccessful) {
                    Log.d("Retrofit", "Resultado actualizado correctamente")
                } else {
                    Log.e("Retrofit", "Error al actualizar resultado: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Resultado>, t: Throwable) {
                Log.e("Retrofit", "Fallo en la conexión: ${t.message}")
            }
        })
}


