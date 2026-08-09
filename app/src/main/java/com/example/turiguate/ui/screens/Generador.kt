package com.example.turiguate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Datos de ejemplo en memoria (Inmutables)
    val itineraryItems = remember { getMockItinerary() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Generador",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
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
                        text = "5 días · Presupuesto medio",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Recomendado") },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = null
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
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
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
 * Genera la lista de 10 elementos de prueba para el itinerario.
 * Reorganizado para 5 días (2 actividades por día).
 */
private fun getMockItinerary(): List<ItineraryItem> {
    return listOf(
        ItineraryItem(
            id = 1, day = 1, destination = "IRTRA de Retalhuleu",
            description = "Visita con entradas, atracciones incluidas, hospedaje y transporte.",
            restaurant = "El Aldeano", hotel = "Aldea de la Selva",
            transport = "4hrs en bus (desde capital)", price = "Q 850.00",
            imageUrl = "https://images.unsplash.com/photo-1596436889106-be35e843f974?q=80&w=600"
        ),
        ItineraryItem(
            id = 2, day = 1, destination = "Antigua Guatemala",
            description = "Tour por iglesias coloniales, museos y cata de café artesanal.",
            restaurant = "La Posada de Don Rodrigo", hotel = "Hotel Casa Santo Domingo",
            transport = "1hr en auto", price = "Q 450.00",
            imageUrl = "https://images.unsplash.com/photo-1590050752117-23a9d7f28243?q=80&w=600"
        ),
        ItineraryItem(
            id = 3, day = 2, destination = "Lago de Atitlán",
            description = "Paseo en lancha por los pueblos de San Juan y Santiago Atitlán.",
            restaurant = "El Pescador", hotel = "Hotel Atitlán",
            transport = "3hrs en bus", price = "Q 600.00",
            imageUrl = "https://images.unsplash.com/photo-1565022699865-c3f25c792484?q=80&w=600"
        ),
        ItineraryItem(
            id = 4, day = 2, destination = "Tikal",
            description = "Exploración de la cuna de la civilización Maya al amanecer.",
            restaurant = "El Mesón del Jaguar", hotel = "Jungle Lodge Tikal",
            transport = "Vuelo interno (1hr)", price = "Q 1,200.00",
            imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?q=80&w=600"
        ),
        ItineraryItem(
            id = 5, day = 3, destination = "Semuc Champey",
            description = "Nadado en pozas naturales de color turquesa y cuevas de K'an Ba.",
            restaurant = "Comedor local", hotel = "Hostal El Portal",
            transport = "8hrs en 4x4", price = "Q 750.00",
            imageUrl = "https://images.unsplash.com/photo-1589991206670-65e3b5e43c51?q=80&w=600"
        ),
        ItineraryItem(
            id = 6, day = 3, destination = "Volcán de Pacaya",
            description = "Caminata moderada para observar ríos de lava y paisajes volcánicos.",
            restaurant = "Pizza del Volcán", hotel = "N/A (Tour de día)",
            transport = "1.5hrs en bus", price = "Q 300.00",
            imageUrl = "https://images.unsplash.com/photo-1544411047-c491e34a2465?q=80&w=600"
        ),
        ItineraryItem(
            id = 7, day = 4, destination = "Monterrico",
            description = "Liberación de tortugas y descanso en playas de arena volcánica.",
            restaurant = "El Pelícano", hotel = "Dos Mundos Pacific",
            transport = "2.5hrs en auto", price = "Q 550.00",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=600"
        ),
        ItineraryItem(
            id = 8, day = 4, destination = "Quetzaltenango",
            description = "Visita al Teatro Municipal y baños termales de Fuentes Georginas.",
            restaurant = "Tertulianos", hotel = "Hotel Pensión Bonifaz",
            transport = "4hrs en bus", price = "Q 400.00",
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?q=80&w=600"
        ),
        ItineraryItem(
            id = 9, day = 5, destination = "Río Dulce",
            description = "Recorrido en lancha por el cañón hacia Livingstone.",
            restaurant = "Marimonte", hotel = "Hotel Nana Juana",
            transport = "5hrs en bus", price = "Q 900.00",
            imageUrl = "https://images.unsplash.com/photo-1568605114967-8130fe3a3699?q=80&w=600"
        ),
        ItineraryItem(
            id = 10, day = 5, destination = "Castillo de San Felipe",
            description = "Exploración de la fortaleza histórica a orillas del Lago de Izabal.",
            restaurant = "Hotel Catamarán", hotel = "G-Land",
            transport = "15 min en lancha", price = "Q 200.00",
            imageUrl = "https://images.unsplash.com/photo-1564501049412-61c2a3083791?q=80&w=600"
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
