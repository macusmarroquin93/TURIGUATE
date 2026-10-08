package com.example.turiguate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.turiguate.ui.screens.CatalogScreen
import com.example.turiguate.ui.screens.DetailScreen
import com.example.turiguate.ui.screens.Generador
import com.example.turiguate.ui.screens.HomeScreen
import com.example.turiguate.ui.theme.TURIGUATETheme

sealed class Screen {
    object Home : Screen()
    object Catalog : Screen()
    data class Generador(val days: Int = 5, val budget: String = "Medio") : Screen()
    data class Detail(val destinationId: Int, val previousScreen: Screen = Catalog) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TURIGUATETheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedDays by remember { mutableIntStateOf(5) }
    var selectedBudget by remember { mutableStateOf("Medio") }

    BackHandler(enabled = currentScreen != Screen.Home) {
        currentScreen = when (val screen = currentScreen) {
            is Screen.Detail -> screen.previousScreen
            is Screen.Generador -> Screen.Home
            is Screen.Catalog -> Screen.Home
            Screen.Home -> Screen.Home
        }
    }

    Scaffold(
        bottomBar = {
            if (currentScreen !is Screen.Detail) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentScreen is Screen.Home,
                        onClick = { currentScreen = Screen.Home },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "Planificar") },
                        label = { Text("Planificar") }
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.Catalog,
                        onClick = { currentScreen = Screen.Catalog },
                        icon = { Icon(Icons.Default.Place, contentDescription = "Catálogo") },
                        label = { Text("Catálogo") }
                    )
                    NavigationBarItem(
                        selected = currentScreen is Screen.Generador,
                        onClick = { currentScreen = Screen.Generador(selectedDays, selectedBudget) },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Itinerario") },
                        label = { Text("Itinerario") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(
                        onGenerateItineraryClick = { days, budget ->
                            selectedDays = days
                            selectedBudget = budget
                            currentScreen = Screen.Generador(days, budget)
                        }
                    )
                }
                is Screen.Catalog -> {
                    CatalogScreen(
                        onBackClick = { currentScreen = Screen.Home },
                        onDestinationClick = { destinationId ->
                            currentScreen = Screen.Detail(destinationId, previousScreen = Screen.Catalog)
                        },
                        onViewItineraryClick = {
                            currentScreen = Screen.Generador(selectedDays, selectedBudget)
                        }
                    )
                }
                is Screen.Generador -> {
                    Generador(
                        days = screen.days,
                        budget = screen.budget,
                        onBackClick = { currentScreen = Screen.Home },
                        onDestinationClick = { destinationId ->
                            currentScreen = Screen.Detail(destinationId, previousScreen = screen)
                        }
                    )
                }
                is Screen.Detail -> {
                    DetailScreen(
                        destinationId = screen.destinationId,
                        onBackClick = { currentScreen = screen.previousScreen },
                        onViewItineraryClick = {
                            currentScreen = Screen.Generador(selectedDays, selectedBudget)
                        }
                    )
                }
            }
        }
    }
}
