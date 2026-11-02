package com.example.turiguate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.turiguate.model.PreferenciasViaje
import com.example.turiguate.ui.screens.DetalleItinerarioScreen
import com.example.turiguate.ui.screens.Generador
import com.example.turiguate.ui.screens.HomeScreen
import com.example.turiguate.ui.theme.TURIGUATETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TURIGUATETheme {
                var currentScreen by remember {
                    mutableStateOf("home")
                }

                var preferencias by remember {
                    mutableStateOf(PreferenciasViaje())
                }

                var itinerarioSeleccionadoId by remember {
                    mutableIntStateOf(0)
                }

                var itinerarioSeleccionadoNombre by remember {
                    mutableStateOf("")
                }

                when (currentScreen) {
                    "home" -> HomeScreen(
                        onGenerateClick = { seleccion ->
                            preferencias = seleccion
                            currentScreen = "generador"
                        },
                        onMisItinerariosClick = {
                            currentScreen = "mis_itinerarios"
                        }
                    )

                    "generador" -> Generador(
                        preferencias = preferencias,
                        onBackClick = {
                            currentScreen = "home"
                        }
                    )

                    "mis_itinerarios" -> PlanScreen(
                        onBackClick = {
                            currentScreen = "home"
                        },
                        onVerItinerario = { id, nombre ->
                            itinerarioSeleccionadoId = id
                            itinerarioSeleccionadoNombre = nombre
                            currentScreen = "detalle_itinerario"
                        }
                    )

                    "detalle_itinerario" -> DetalleItinerarioScreen(
                        itinerarioId = itinerarioSeleccionadoId,
                        nombre = itinerarioSeleccionadoNombre,
                        onBackClick = {
                            currentScreen = "mis_itinerarios"
                        }
                    )
                }
            }
        }
    }
}