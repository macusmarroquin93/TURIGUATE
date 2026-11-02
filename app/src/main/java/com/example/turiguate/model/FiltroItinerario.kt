package com.example.turiguate.model

fun filtrarPorIntereses(
    destinos: List<ItineraryItem>,
    intereses: List<String>
): List<ItineraryItem> {
    // Categorías que usará nuestra aplicación.
    val categorias = mapOf(
        "Antigua Guatemala" to setOf(
            "Cultura", "Historia", "Gastronomía"
        ),
        "Lago de Atitlán" to setOf(
            "Naturaleza", "Cultura", "Descanso"
        ),
        "Tikal" to setOf(
            "Cultura", "Historia", "Naturaleza"
        ),
        "Quiriguá" to setOf(
            "Cultura", "Historia"
        ),
        "Semuc Champey" to setOf(
            "Naturaleza", "Aventura"
        ),
        "Laguna Lachuá" to setOf(
            "Naturaleza", "Descanso"
        ),
        "Biotopo del Quetzal" to setOf(
            "Naturaleza", "Aventura"
        ),
        "Volcán de Pacaya" to setOf(
            "Naturaleza", "Aventura", "Deportes"
        ),
        "Volcán Acatenango" to setOf(
            "Naturaleza", "Aventura", "Deportes"
        ),
        "Monterrico" to setOf(
            "Playa", "Descanso", "Naturaleza"
        ),
        "Río Dulce" to setOf(
            "Naturaleza", "Aventura", "Descanso"
        ),
        "El Paredón" to setOf(
            "Playa", "Descanso", "Deportes"
        ),
        "IRTRA de Retalhuleu" to setOf(
            "Aventura", "Descanso"
        ),
        "Castillo de San Felipe" to setOf(
            "Cultura", "Historia"
        ),
        "Quetzaltenango" to setOf(
            "Cultura", "Historia", "Gastronomía"
        )
    )

    // Sin intereses específicos mostramos todas las opciones.
    // "Otros" permite explorar cuando se elige solo.
    val seleccionados = intereses
        .filterNot { it == "Otros" }
        .toSet()

    if (seleccionados.isEmpty()) {
        return destinos
    }

    return destinos
        .filter { destino ->
            categorias[destino.destination]
                .orEmpty()
                .any { it in seleccionados }
        }
        .sortedByDescending { destino ->
            // Primero los lugares que coinciden con más intereses.
            categorias[destino.destination]
                .orEmpty()
                .count { it in seleccionados }
        }
}