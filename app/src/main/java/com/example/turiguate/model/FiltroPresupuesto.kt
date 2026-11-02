package com.example.turiguate.model

fun filtrarPorPresupuesto(
    destinos: List<ItineraryItem>,
    presupuesto: String
): List<ItineraryItem> {
    val limite = when (presupuesto) {
        "Bajo costo" -> 450.0
        "Medio" -> 850.0
        "Alto" -> Double.POSITIVE_INFINITY
        else -> return emptyList()
    }

    return destinos.filter { destino ->
        val precio = destino.price
            .replace("Q", "")
            .replace(",", "")
            .trim()
            .toDoubleOrNull()

        precio != null && precio >= 0 && precio <= limite
    }
}
