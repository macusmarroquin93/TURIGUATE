package com.example.turiguate.model

import com.example.turiguate.data.DailyCosts
import com.example.turiguate.data.LocalCatalog
import java.util.Locale
import kotlin.math.abs

fun formatearQuetzales(monto: Double): String =
    String.format(Locale.US, "Q %,.2f", monto)

private data class OpcionesDeCostos(
    val bajo: DailyCosts,
    val medio: DailyCosts,
    val alto: DailyCosts
)

// Estimaciones de ejemplo por persona al día.
// La estadía corresponde a una noche.
// No incluyen transporte.
private val opcionesPorDestino = mapOf(
    "antigua" to OpcionesDeCostos(
        bajo = DailyCosts(
            food = 60.0,
            accommodation = 90.0,
            activities = 25.0
        ),
        medio = DailyCosts(
            food = 100.0,
            accommodation = 175.0,
            activities = 75.0
        ),
        alto = DailyCosts(
            food = 200.0,
            accommodation = 450.0,
            activities = 175.0
        )
    ),

    "atitlan" to OpcionesDeCostos(
        bajo = DailyCosts(
            food = 60.0,
            accommodation = 85.0,
            activities = 40.0
        ),
        medio = DailyCosts(
            food = 90.0,
            accommodation = 150.0,
            activities = 100.0
        ),
        alto = DailyCosts(
            food = 180.0,
            accommodation = 400.0,
            activities = 200.0
        )
    ),

    "monterrico" to OpcionesDeCostos(
        bajo = DailyCosts(
            food = 65.0,
            accommodation = 100.0,
            activities = 25.0
        ),
        medio = DailyCosts(
            food = 100.0,
            accommodation = 175.0,
            activities = 75.0
        ),
        alto = DailyCosts(
            food = 200.0,
            accommodation = 450.0,
            activities = 150.0
        )
    ),

    "quetzaltenango" to OpcionesDeCostos(
        bajo = DailyCosts(
            food = 55.0,
            accommodation = 80.0,
            activities = 30.0
        ),
        medio = DailyCosts(
            food = 90.0,
            accommodation = 150.0,
            activities = 100.0
        ),
        alto = DailyCosts(
            food = 175.0,
            accommodation = 350.0,
            activities = 175.0
        )
    )
)

private val costosAdicionales = mapOf(
    "irtra" to DailyCosts(
        food = 125.0,
        accommodation = 400.0,
        activities = 325.0
    ),
    "castillo_san_felipe" to DailyCosts(
        food = 80.0,
        accommodation = 125.0,
        activities = 75.0
    )
)

private fun identificarDestino(
    item: ItineraryItem
): String =
    item.imageUrl
        .substringBefore("?")
        .substringAfterLast("/")
        .substringBeforeLast(".")

fun obtenerCostosItinerario(
    item: ItineraryItem,
    presupuesto: String = "Medio"
): DailyCosts? {
    val destinationId = identificarDestino(item)
    val opciones = opcionesPorDestino[destinationId]

    if (opciones != null) {
        return when (presupuesto) {
            "Bajo costo" -> opciones.bajo
            "Medio" -> opciones.medio
            "Alto" -> opciones.alto
            else -> null
        }
    }

    return LocalCatalog.destinations
        .find { it.id == destinationId }
        ?.dailyCosts
        ?: costosAdicionales[destinationId]
}

// Encuentra el desglose que coincide con el precio del itinerario.
// También permite mostrar los itinerarios guardados usando
// las estimaciones actuales, sin cambiar la estructura de Room.
fun obtenerCostosParaMostrar(
    item: ItineraryItem
): DailyCosts? {
    val precio = item.price
        .replace("Q", "")
        .replace(",", "")
        .trim()
        .toDoubleOrNull()
        ?: return null

    return listOf("Bajo costo", "Medio", "Alto")
        .mapNotNull { presupuesto ->
            obtenerCostosItinerario(
                item = item,
                presupuesto = presupuesto
            )
        }
        .firstOrNull { costos ->
            abs(costos.total - precio) < 0.005
        }
}