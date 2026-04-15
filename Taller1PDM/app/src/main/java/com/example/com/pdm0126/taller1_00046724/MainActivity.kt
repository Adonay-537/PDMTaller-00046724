package com.example.com.pdm0126.taller1_00046724

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppAndroidPedia()
                }
            }
        }
    }
}


enum class PantallasApp {
    BIENVENIDA, QUIZ, RESULTADOS
}

@Composable
fun AppAndroidPedia() {
    var pantallaActual by remember { mutableStateOf(PantallasApp.BIENVENIDA) }
    var puntaje by remember { mutableStateOf(0) }

    when (pantallaActual) {
        PantallasApp.BIENVENIDA -> {
            PantallaBienvenida(
                alComenzar = {
                    puntaje = 0
                    pantallaActual = PantallasApp.QUIZ
                }
            )
        }
        PantallasApp.QUIZ -> {
            PantallaQuiz(
                puntaje = puntaje,
                alCambiarPuntaje = { nuevoPuntaje -> puntaje = nuevoPuntaje },
                alTerminar = { pantallaActual = PantallasApp.RESULTADOS }
            )
        }
        PantallasApp.RESULTADOS -> {
            PantallaResultados(
                puntaje = puntaje,
                alReiniciar = {
                    puntaje = 0
                    pantallaActual = PantallasApp.QUIZ
                }
            )
        }
    }
}


@Composable
fun PantallaBienvenida(alComenzar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AndroidPedia",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "¿Cuánto sabes de Android?", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "Estudiante: CHRISTIAN ADONAY PENADO DIAZ")
        Text(text = "Carnet: 000046724")

        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = alComenzar,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Comenzar Quiz", fontSize = 18.sp)
        }
    }
}


@Composable
fun PantallaQuiz(puntaje: Int, alCambiarPuntaje: (Int) -> Unit, alTerminar: () -> Unit) {
    var indiceActual by remember { mutableStateOf(0) }
    var opcionSeleccionada by remember { mutableStateOf<String?>(null) }
    var yaRespondio by remember { mutableStateOf(false) }

    val preguntaActual = quizQuestions[indiceActual]
    val totalPreguntas = quizQuestions.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pregunta ${indiceActual + 1} de $totalPreguntas", fontWeight = FontWeight.Bold)
            Text("Puntaje: $puntaje/$totalPreguntas", fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = preguntaActual.question,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Medium
            )
        }

        preguntaActual.options.forEach { opcion ->
            val esCorrecta = opcion == preguntaActual.correctAnswer

            val colorBoton = if (yaRespondio) {
                if (esCorrecta) Color(0xFF4CAF50)
                else if (opcion == opcionSeleccionada) Color(0xFFF44336)
                else Color.LightGray

            } else {
                MaterialTheme.colorScheme.primary
            }

            Button(
                onClick = {
                    if (!yaRespondio) {
                        opcionSeleccionada = opcion
                        yaRespondio = true
                        if (esCorrecta) alCambiarPuntaje(puntaje + 1)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = colorBoton,
                    disabledContainerColor = colorBoton
                ),
                enabled = !yaRespondio
            ) {
                Text(
                    text = opcion,
                    color = if (yaRespondio && !esCorrecta && opcion != opcionSeleccionada) Color.Black else Color.White
                )
            }
        }

        if (yaRespondio) {
            Spacer(modifier = Modifier.height(24.dp))
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Text(text = "💡 Dato curioso: ${preguntaActual.funFact}", modifier = Modifier.padding(16.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (indiceActual < totalPreguntas - 1) {
                        indiceActual++
                        opcionSeleccionada = null
                        yaRespondio = false
                    } else {
                        alTerminar()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (indiceActual < totalPreguntas - 1) "Siguiente" else "Ver Resultado")
            }
        }
    }
}


@Composable
fun PantallaResultados(puntaje: Int, alReiniciar: () -> Unit) {
    val mensaje = when (puntaje) {
        3 -> "¡Excelente! Ahora sabes un poco de la historia de Android."
        2 -> "¡Nada mal! Estuviste cerca, un error cualquiera lo comete."
        1 -> "Ya va queriendo, hechale una leida al doc, y vuelve a intentarlo."
        else -> "¡Tranquilo! No te desanimes, esfuerzate en lograr un puntaje perfecto a la proxima."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Quiz Terminado!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Obtuviste $puntaje de 3",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = mensaje,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = alReiniciar,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Reiniciar Quiz", fontSize = 18.sp)
        }
    }
}