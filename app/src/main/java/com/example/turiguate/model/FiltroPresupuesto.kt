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

    return destinos.mapNotNull { destino ->
        val costos = obtenerCostosItinerario(
            item = destino,
            presupuesto = presupuesto
        ) ?: return@mapNotNull null

        if (costos.total > limite) {
            return@mapNotNull null
        }

        destino.copy(
            price = formatearQuetzales(costos.total),

            // Son tipos de servicio orientativos;
            // no son cotizaciones de establecimientos específicos.
            restaurant = when (presupuesto) {
                "Bajo costo" -> "Comedor o mercado local"
                "Medio" -> "Restaurante de precio medio"
                else -> "Restaurante de categoría superior"
            },

            hotel = when (presupuesto) {
                "Bajo costo" -> "Hostal o alojamiento económico"
                "Medio" -> "Hotel de precio medio"
                else -> "Hotel de categoría superior"
            },

            transport = if (
                destino.transport.isBlank() ||
                destino.transport == "Por definir"
            ) {
                "A coordinar; costo no incluido"
            } else {
                destino.transport
            }
        )
    }
}