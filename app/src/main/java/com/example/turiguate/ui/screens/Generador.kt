
package com.example.turiguate.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.turiguate.TuriguateApplication
import com.example.turiguate.model.ItineraryItem
import com.example.turiguate.ui.components.ItineraryCard
import com.example.turiguate.ui.viewmodel.ItinerarioViewModel
import com.example.turiguate.ui.viewmodel.ItinerarioViewModelFactory
import kotlinx.coroutines.launch
import com.example.turiguate.model.PreferenciasViaje
import com.example.turiguate.model.filtrarPorIntereses
import com.example.turiguate.model.filtrarPorPresupuesto
import com.example.turiguate.model.formatearQuetzales
import com.example.turiguate.model.obtenerCostosItinerario

/**
 * Pantalla Generador.
 *
 * Muestra las actividades turísticas y permite
 * guardar el itinerario completo utilizando Room.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Generador(
    onBackClick: () -> Unit,
    preferencias: PreferenciasViaje = PreferenciasViaje()
){
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Obtener el Repository desde Application
    val application = context.applicationContext
            as TuriguateApplication

    // Crear el ViewModel mediante nuestra Factory
    val itinerarioViewModel: ItinerarioViewModel = viewModel(
        factory = ItinerarioViewModelFactory(
            application.itinerarioRepository
        )
    )

    // Estado del proceso de guardado
    var guardando by remember {
        mutableStateOf(false)
    }

    // Actividades
    val opciones = remember(preferencias) {
        val destinosPorInteres = filtrarPorIntereses(
            destinos = getBaseItinerary(),
            intereses = preferencias.intereses
        )

        val destinos = filtrarPorPresupuesto(
            destinos = destinosPorInteres,
            presupuesto = preferencias.presupuesto
        )

        if (destinos.isEmpty()) {
            return@remember emptyList<List<ItineraryItem>>()
        }

        destinos.indices.take(3).map { desplazamiento ->
            val orden = destinos.drop(desplazamiento) +
                    destinos.take(desplazamiento)

            List(preferencias.dias) { indice ->
                val destino = orden.getOrNull(indice)

                if (destino != null) {
                    destino.copy(
                        id = indice + 1,
                        day = indice + 1
                    )
                } else {
                    val ultimoDestino = orden.last()

                    // Si faltan destinos para completar el viaje,
                    // se agrega otro día en el último lugar.
                    // Conserva sus costos, imagen y hospedaje.
                    ultimoDestino.copy(
                        id = indice + 1,
                        day = indice + 1,
                        destination =
                            "Día adicional en ${ultimoDestino.destination}",
                        description =
                            "Continúa tu estancia en este destino. " +
                                    "Se mantiene el presupuesto diario de comida, " +
                                    "estadía y actividades.",
                        transport =
                            "Permanencia en el destino; transporte no incluido"
                    )
                }
            }
        }.distinctBy { itinerario ->
            itinerario.map { it.destination }
        }
    }

    var opcionSeleccionada by remember(preferencias) {
        mutableIntStateOf(0)
    }

    val itineraryItems = opciones.getOrElse(opcionSeleccionada) {
        emptyList<ItineraryItem>()
    }

    val precios = itineraryItems.map { actividad ->
        actividad.price
            .replace("Q", "")
            .replace(",", "")
            .trim()
            .toDoubleOrNull()
    }

    val totalEstimado = precios.filterNotNull().sum()

    val hayCostosPendientes = precios.any { it == null }

    val montoFormateado = String.format(
        java.util.Locale.US,
        "Q %,.2f",
        totalEstimado
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Generador",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
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
                        text = "${preferencias.dias} días · Presupuesto ${preferencias.presupuesto}\n" +
                                if (hayCostosPendientes) {
                                    "Subtotal estimado: $montoFormateado · Hay costos por definir"
                                } else {
                                    "Total estimado: $montoFormateado"
                                },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                if (opciones.isEmpty()) {
                                    "Sin coincidencias"
                                } else {
                                    "Opción ${opcionSeleccionada + 1} de ${opciones.size}"
                                }
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = null
                    )
                }
            }

            // Lista de actividades turísticas
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (itineraryItems.isEmpty()) {
                    item {
                        Text(
                            text = "No hay actividades con precio conocido " +
                                    "que coincidan con tus intereses y presupuesto. " +
                                    "Prueba otro presupuesto o cambia tus intereses.",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(
                    items = itineraryItems,
                    key = { item -> item.id }
                ) { item ->

                    ItineraryCard(
                        item = item,
                        onClick = { destinationName ->

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "$destinationName seleccionado"
                                )
                            }
                        }
                    )
                }

                // Botones al final del itinerario
                item {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    // Botón original
                    Button(
                        onClick = {
                            opcionSeleccionada =
                                (opcionSeleccionada + 1) % opciones.size
                        },
                        enabled = opciones.size > 1 && !guardando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = "Ver otra opción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    // Guardar itinerario en Room
                    Button(
                        onClick = {

                            guardando = true

                            itinerarioViewModel.guardarItinerarioGenerado(
                                nombre = "Viaje de ${preferencias.dias} días" +
                                        if (hayCostosPendientes) " (costos pendientes)" else "",
                                destino = "Guatemala",
                                fecha = "2026-10-15",
                                presupuesto = totalEstimado,
                                actividades = itineraryItems,

                                onGuardado = {
                                    guardando = false

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Itinerario guardado correctamente"
                                        )
                                    }
                                },

                                onError = { error ->
                                    guardando = false

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Error al guardar: ${error.message}"
                                        )
                                    }
                                }
                            )
                        },
                        enabled = !guardando && itineraryItems.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        if (guardando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Guardar itinerario",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}

/**
 * Datos de destinos
 **
 */

private fun getBaseItinerary(): List<ItineraryItem> {
    return listOf(
        ItineraryItem(
            id = 1,
            day = 1,
            destination = "Antigua Guatemala",
            description = "Recorre calles empedradas, iglesias coloniales, museos y plazas.",
            restaurant = "La Posada de Don Rodrigo",
            hotel = "Hotel Casa Santo Domingo",
            transport = "1hr en auto",
            price = "",
            imageUrl = "file:///android_asset/destinations/antigua.jpg"
        ),
        ItineraryItem(
            id = 2,
            day = 1,
            destination = "Lago de Atitlán",
            description = "Disfruta del paisaje del lago y conoce los pueblos de sus alrededores.",
            restaurant = "El Pescador",
            hotel = "Hotel Atitlán",
            transport = "3hrs en bus",
            price = "",
            imageUrl = "file:///android_asset/destinations/atitlan.jpg"
        ),
        ItineraryItem(
            id = 3,
            day = 2,
            destination = "Tikal",
            description = "Explora una antigua ciudad maya rodeada de selva, templos y plazas.",
            restaurant = "El Mesón del Jaguar",
            hotel = "Jungle Lodge Tikal",
            transport = "Vuelo interno (1hr)",
            price = "",
            imageUrl = "file:///android_asset/destinations/tikal.jpg"
        ),
        ItineraryItem(
            id = 4,
            day = 2,
            destination = "Quiriguá",
            description = "Conoce un sitio arqueológico maya reconocido por sus estelas talladas.",
            restaurant = "Por definir",
            hotel = "Por definir",
            transport = "Por definir",
            price = "",
            imageUrl = "file:///android_asset/destinations/quirigua.jpg"
        ),
        ItineraryItem(
            id = 5,
            day = 3,
            destination = "Semuc Champey",
            description = "Descubre pozas naturales y senderos rodeados de vegetación.",
            restaurant = "Comedor local",
            hotel = "Hostal El Portal",
            transport = "8hrs en 4x4",
            price = "",
            imageUrl = "file:///android_asset/destinations/semuc.jpg"
        ),
        ItineraryItem(
            id = 6,
            day = 3,
            destination = "Laguna Lachuá",
            description = "Descubre una laguna rodeada de bosque tropical y observa la naturaleza.",
            restaurant = "Por definir",
            hotel = "Por definir",
            transport = "Por definir",
            price = "",
            imageUrl = "file:///android_asset/destinations/lachua.jpg"
        ),
        ItineraryItem(
            id = 7,
            day = 3,
            destination = "Biotopo del Quetzal",
            description = "Recorre senderos de bosque nuboso y conoce el hábitat del quetzal.",
            restaurant = "Por definir",
            hotel = "Por definir",
            transport = "Por definir",
            price = "",
            imageUrl = "file:///android_asset/destinations/biotopo.jpg"
        ),
        ItineraryItem(
            id = 8,
            day = 4,
            destination = "Volcán de Pacaya",
            description = "Conoce paisajes volcánicos y senderos de montaña.",
            restaurant = "Pizza del Volcán",
            hotel = "N/A (Tour de día)",
            transport = "1.5hrs en bus",
            price = "",
            imageUrl = "file:///android_asset/destinations/pacaya.jpg"
        ),
        ItineraryItem(
            id = 9,
            day = 4,
            destination = "Volcán Acatenango",
            description = "Un destino de montaña para caminatas exigentes y paisajes volcánicos.",
            restaurant = "Por definir",
            hotel = "Por definir",
            transport = "Por definir",
            price = "",
            imageUrl = "file:///android_asset/destinations/acatenango.jpg"
        ),
        ItineraryItem(
            id = 10,
            day = 5,
            destination = "Monterrico",
            description = "Disfruta de las playas de arena oscura y los paisajes del Pacífico.",
            restaurant = "El Pelícano",
            hotel = "Dos Mundos Pacific",
            transport = "2.5hrs en auto",
            price = "",
            imageUrl = "file:///android_asset/destinations/monterrico.jpg"
        ),
        ItineraryItem(
            id = 11,
            day = 5,
            destination = "Río Dulce",
            description = "Conoce paisajes de río, vegetación tropical y recorridos por el agua.",
            restaurant = "Marimonte",
            hotel = "Hotel Nana Juana",
            transport = "5hrs en bus",
            price = "",
            imageUrl = "file:///android_asset/destinations/rio_dulce.jpg"
        ),
        ItineraryItem(
            id = 12,
            day = 5,
            destination = "El Paredón",
            description = "Conoce una comunidad costera del Pacífico, sus playas y ambiente de surf.",
            restaurant = "Por definir",
            hotel = "Por definir",
            transport = "Por definir",
            price = "",
            imageUrl = "file:///android_asset/destinations/el_paredon.jpg"
        ),
        ItineraryItem(
            id = 13,
            day = 5,
            destination = "IRTRA de Retalhuleu",
            description = "Visita con entradas, atracciones incluidas, hospedaje y transporte.",
            restaurant = "El Aldeano",
            hotel = "Aldea de la Selva",
            transport = "4hrs en bus (desde capital)",
            price = "",
            imageUrl = "file:///android_asset/destinations/irtra.jpg"
        ),
        ItineraryItem(
            id = 14,
            day = 5,
            destination = "Castillo de San Felipe",
            description = "Exploración de la fortaleza histórica a orillas del Lago de Izabal.",
            restaurant = "Hotel Catamarán",
            hotel = "G-Land",
            transport = "15 min en lancha",
            price = "",
            imageUrl = "file:///android_asset/destinations/castillo_san_felipe.jpg"
        ),
        ItineraryItem(
            id = 15,
            day = 5,
            destination = "Quetzaltenango",
            description = "Visita al Teatro Municipal y baños termales de Fuentes Georginas.",
            restaurant = "Tertulianos",
            hotel = "Hotel Pensión Bonifaz",
            transport = "4hrs en bus",
            price = "",
            imageUrl = "file:///android_asset/destinations/quetzaltenango.jpg"
        )
    )
}
private fun getMockItinerary(): List<ItineraryItem> {
    return getBaseItinerary().map { item ->
        val costos = requireNotNull(
            obtenerCostosItinerario(item)
        ) {
            "Faltan los costos de ${item.destination}"
        }

        item.copy(
            price = formatearQuetzales(costos.total),

            restaurant = if (
                item.restaurant.isBlank() ||
                item.restaurant == "Por definir"
            ) {
                "Comedor local a elección"
            } else {
                item.restaurant
            },

            hotel = if (
                item.hotel.isBlank() ||
                item.hotel == "Por definir" ||
                item.hotel.startsWith("N/A")
            ) {
                "Hospedaje local a elección"
            } else {
                item.hotel
            },

            transport = if (
                item.transport.isBlank() ||
                item.transport == "Por definir"
            ) {
                "A coordinar; costo no incluido"
            } else {
                item.transport
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GeneradorPreview() {
    MaterialTheme {
        Generador(onBackClick = {})
    }
}
