package com.example.turiguate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.turiguate.R
import com.example.turiguate.model.ItineraryItem
import com.example.turiguate.ui.components.ItineraryCard
import kotlinx.coroutines.launch

/**
 * Pantalla Generador: Muestra el itinerario detallado.
 * Implementa una LazyColumn con 10 elementos y manejo de estado para Snackbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Generador(
    days: Int = 5,
    budget: String = "Medio",
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onDestinationClick: (Int) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Factor de ajuste según el presupuesto seleccionado
    val budgetMultiplier = remember(budget) {
        when (budget.lowercase()) {
            "bajo costo", "bajo" -> 0.75
            "alto" -> 1.40
            else -> 1.00
        }
    }

    // Datos de ejemplo en memoria adaptados dinámicamente a días y presupuesto
    val itineraryItems = remember(days, budgetMultiplier) {
        generateDynamicItinerary(days, budgetMultiplier)
    }

    val totalCost = remember(itineraryItems) {
        itineraryItems.sumOf { item ->
            item.price.replace("Q", "").replace(",", "").trim().toDoubleOrNull() ?: 0.0
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mi Itinerario",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkTheme) "Cambiar a modo claro" else "Cambiar a modo oscuro"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Resumen del viaje
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$days días · Presupuesto ${budget.lowercase()}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Total estimado: Q %,.2f".format(totalCost),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Lista de itinerario usando LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = itineraryItems,
                    key = { item -> item.id }
                ) { item ->
                    ItineraryCard(
                        item = item,
                        onClick = { destinationName ->
                            onDestinationClick(item.id)
                            scope.launch {
                                snackbarHostState.showSnackbar("$destinationName seleccionado")
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* Acción futura */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            "Comparar opciones",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * Genera el itinerario dinámicamente para cualquier número de días (ej. 1 a 30 días).
 */
private fun generateDynamicItinerary(days: Int, multiplier: Double): List<ItineraryItem> {
    val templates = getMockItinerary()
    val result = mutableListOf<ItineraryItem>()
    var counter = 0

    for (day in 1..days) {
        repeat(2) {
            val template = templates[counter % templates.size]
            val basePrice = template.price.replace("Q", "").replace(",", "").trim().toDoubleOrNull() ?: 0.0
            val adjustedPrice = basePrice * multiplier

            result.add(
                template.copy(
                    id = counter + 1,
                    day = day,
                    price = "Q %,.2f".format(adjustedPrice)
                )
            )
            counter++
        }
    }
    return result
}

/**
 * Genera la lista de 10 elementos de plantilla para el itinerario.
 */
private fun getMockItinerary(): List<ItineraryItem> {
    return listOf(
        ItineraryItem(
            id = 1, day = 1, destination = "IRTRA de Retalhuleu",
            description = "Visita con entradas, atracciones incluidas, hospedaje y transporte.",
            restaurant = "El Aldeano", hotel = "Aldea de la Selva",
            transport = "4hrs en bus (desde capital)", price = "Q 650.00",
            imageRes = R.drawable.irtra
        ),
        ItineraryItem(
            id = 2, day = 1, destination = "Antigua Guatemala",
            description = "Tour por iglesias coloniales, museos y cata de café artesanal.",
            restaurant = "La Posada de Don Rodrigo", hotel = "Hotel Casa Santo Domingo",
            transport = "1hr en auto", price = "Q 350.00",
            imageRes = R.drawable.antigua
        ),
        ItineraryItem(
            id = 3, day = 2, destination = "Lago de Atitlán",
            description = "Paseo en lancha por los pueblos de San Juan y Santiago Atitlán.",
            restaurant = "El Pescador", hotel = "Hotel Atitlán",
            transport = "3hrs en bus", price = "Q 350.00",
            imageRes = R.drawable.atitlan
        ),
        ItineraryItem(
            id = 4, day = 2, destination = "Tikal",
            description = "Exploración de la cuna de la civilización Maya al amanecer.",
            restaurant = "El Mesón del Jaguar", hotel = "Jungle Lodge Tikal",
            transport = "Vuelo interno (1hr)", price = "Q 450.00",
            imageRes = R.drawable.tikal
        ),
        ItineraryItem(
            id = 5, day = 3, destination = "Semuc Champey",
            description = "Nadado en pozas naturales de color turquesa y cuevas de K'an Ba.",
            restaurant = "Comedor local", hotel = "Hostal El Portal",
            transport = "8hrs en 4x4", price = "Q 300.00",
            imageRes = R.drawable.semuc_champey
        ),
        ItineraryItem(
            id = 6, day = 3, destination = "Volcán de Pacaya",
            description = "Caminata moderada para observar ríos de lava y paisajes volcánicos.",
            restaurant = "Pizza del Volcán", hotel = "N/A (Tour de día)",
            transport = "1.5hrs en bus", price = "Q 250.00",
            imageRes = R.drawable.pacaya
        ),
        ItineraryItem(
            id = 7, day = 4, destination = "Monterrico",
            description = "Liberación de tortugas y descanso en playas de arena volcánica.",
            restaurant = "El Pelícano", hotel = "Dos Mundos Pacific",
            transport = "2.5hrs en auto", price = "Q 350.00",
            imageRes = R.drawable.monterrico
        ),
        ItineraryItem(
            id = 8, day = 4, destination = "Quetzaltenango",
            description = "Visita al Teatro Municipal y baños termales de Fuentes Georginas.",
            restaurant = "Tertulianos", hotel = "Hotel Pensión Bonifaz",
            transport = "4hrs en bus", price = "Q 250.00",
            imageRes = R.drawable.xela
        ),
        ItineraryItem(
            id = 9, day = 5, destination = "Río Dulce",
            description = "Recorrido en lancha por el cañón hacia Livingstone.",
            restaurant = "Marimonte", hotel = "Hotel Nana Juana",
            transport = "5hrs en bus", price = "Q 400.00",
            imageRes = R.drawable.rio_dulce
        ),
        ItineraryItem(
            id = 10, day = 5, destination = "Castillo de San Felipe",
            description = "Exploración de la fortaleza histórica a orillas del Lago de Izabal.",
            restaurant = "Hotel Catamarán", hotel = "G-Land",
            transport = "15 min en lancha", price = "Q 100.00",
            imageRes = R.drawable.castillo_felipe
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GeneradorPreview() {
    MaterialTheme {
        Generador(onBackClick = {})
    }
}
