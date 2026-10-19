
package com.example.turiguate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.turiguate.ui.screens.Generador
import com.example.turiguate.ui.screens.HomeScreen
import com.example.turiguate.ui.theme.TURIGUATETheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TURIGUATETheme {

                // Navegación mediante estado de Compose
                var currentScreen by remember {
                    mutableStateOf("home")
                }

                when (currentScreen) {

                    // Pantalla principal
                    "home" -> HomeScreen(
                        onGenerateClick = {
                            currentScreen = "generador"
                        },
                        onMisItinerariosClick = {
                            currentScreen = "mis_itinerarios"
                        }
                    )

                    // Pantalla del generador de itinerarios
                    "generador" -> Generador(
                        onBackClick = {
                            currentScreen = "home"
                        }
                    )

                    // Pantalla de itinerarios guardados en Room
                    "mis_itinerarios" -> PlanScreen(
                        onBackClick = {
                            currentScreen = "home"
                        }
                    )
                }
            }
        }
    }
}
