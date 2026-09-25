package com.example.turiguate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import com.example.turiguate.ui.screens.CatalogScreen
import com.example.turiguate.ui.screens.DetailScreen
import com.example.turiguate.ui.screens.Generador
import com.example.turiguate.ui.screens.HomeScreen
import com.example.turiguate.ui.theme.TURIGUATETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemInDark = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemInDark) }
            var currentScreen by remember { mutableStateOf("home") }
            var selectedDestinationId by remember { mutableIntStateOf(1) }

            TURIGUATETheme(darkTheme = isDarkTheme) {
                when (currentScreen) {
                    "home" -> HomeScreen(
                        onGenerateClick = { currentScreen = "generador" },
                        onCatalogClick = { currentScreen = "catalog" },
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = !isDarkTheme }
                    )
                    "generador" -> Generador(
                        onBackClick = { currentScreen = "home" },
                        onDestinationClick = { id ->
                            selectedDestinationId = id
                            currentScreen = "detail"
                        }
                    )
                    "catalog" -> CatalogScreen(
                        onBackClick = { currentScreen = "home" },
                        onDestinationClick = { id ->
                            selectedDestinationId = id
                            currentScreen = "detail"
                        },
                        onViewItineraryClick = { currentScreen = "generador" }
                    )
                    "detail" -> DetailScreen(
                        destinationId = selectedDestinationId,
                        onBackClick = { currentScreen = "catalog" },
                        onViewItineraryClick = { currentScreen = "generador" }
                    )
                }
            }
        }
    }
}
