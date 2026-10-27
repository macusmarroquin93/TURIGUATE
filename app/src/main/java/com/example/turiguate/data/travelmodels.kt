package com.example.turiguate.data


import java.text.Normalizer
enum class Category(val label: String) {
    CULTURE("Cultura"),
    NATURE("Naturaleza"),
    ADVENTURE("Aventura"),
    BEACH("Playa")
}

data class Destination(
    val id: String,
    val name: String,
    val department: String,
    val category: Category,
    val description: String
)

data class TravelPlan(
    val id: String,
    val title: String,
    val days: Int,
    val destinationIds: List<String>
) {
    init {
        require(id.isNotBlank()) { "El plan necesita un identificador." }
        require(title.isNotBlank() && title.length <= 80) {
            "El nombre debe tener entre 1 y 80 caracteres."
        }
        require(days in 1..30) { "El viaje debe durar entre 1 y 30 días." }
        require(destinationIds.isNotEmpty()) {
            "Selecciona al menos un destino."
        }
        require(destinationIds.all { it.isNotBlank() }) {
            "Hay un destino inválido."
        }
        require(destinationIds.distinct().size == destinationIds.size) {
            "Un destino no puede repetirse dentro del plan."
        }
    }
}

object LocalCatalog {
    val destinations = listOf(
        Destination(
            id = "antigua",
            name = "Antigua Guatemala",
            department = "Sacatepéquez",
            category = Category.CULTURE,
            description = "Recorre calles empedradas, plazas y edificios " +
                    "coloniales. Una opción para disfrutar de historia, " +
                    "arquitectura y cultura."
        ),
        Destination(
            id = "tikal",
            name = "Tikal",
            department = "Petén",
            category = Category.CULTURE,
            description = "Explora una antigua ciudad maya rodeada de selva. " +
                    "Descubre sus templos, plazas y patrimonio arqueológico."
        ),
        Destination(
            id = "atitlan",
            name = "Lago de Atitlán",
            department = "Sololá",
            category = Category.NATURE,
            description = "Disfruta del paisaje del lago y conoce los pueblos " +
                    "de sus alrededores, entre montañas y volcanes."
        ),
        Destination(
            id = "semuc",
            name = "Semuc Champey",
            department = "Alta Verapaz",
            category = Category.NATURE,
            description = "Descubre pozas naturales y senderos rodeados " +
                    "de vegetación en un paisaje de aguas turquesas."
        ),
        Destination(
            id = "pacaya",
            name = "Volcán de Pacaya",
            department = "Escuintla",
            category = Category.ADVENTURE,
            description = "Conoce paisajes volcánicos y senderos de montaña. " +
                    "Consulta las condiciones de acceso antes de organizar la visita."
        ),
        Destination(
            id = "monterrico",
            name = "Monterrico",
            department = "Santa Rosa",
            category = Category.BEACH,
            description = "Disfruta de la costa del Pacífico, sus playas " +
                    "de arena oscura y sus paisajes costeros."
        ),
        Destination(
            id = "quirigua",
            name = "Quiriguá",
            department = "Izabal",
            category = Category.CULTURE,
            description = "Conoce un sitio arqueológico maya reconocido " +
                    "por sus estelas talladas y su historia."
        ),
        Destination(
            id = "lachua",
            name = "Laguna Lachuá",
            department = "Alta Verapaz",
            category = Category.NATURE,
            description = "Descubre una laguna rodeada de bosque tropical. " +
                    "Una opción para apreciar paisajes y observar la naturaleza."
        ),
        Destination(
            id = "rio_dulce",
            name = "Río Dulce",
            department = "Izabal",
            category = Category.NATURE,
            description = "Conoce paisajes de río, vegetación tropical " +
                    "y recorridos por el agua en la región de Izabal."
        ),
        Destination(
            id = "biotopo",
            name = "Biotopo del Quetzal",
            department = "Baja Verapaz",
            category = Category.NATURE,
            description = "Recorre senderos de bosque nuboso y descubre " +
                    "un espacio dedicado a la conservación del hábitat del quetzal."
        ),
        Destination(
            id = "acatenango",
            name = "Volcán Acatenango",
            department = "Chimaltenango",
            category = Category.ADVENTURE,
            description = "Un destino de montaña para caminatas exigentes. " +
                    "Planifica la visita con preparación, acompañamiento " +
                    "y revisión de las condiciones locales."
        ),
        Destination(
            id = "el_paredon",
            name = "El Paredón",
            department = "Escuintla",
            category = Category.BEACH,
            description = "Conoce una comunidad costera del Pacífico, " +
                    "sus playas y su ambiente relacionado con el surf."
        )
    )
}

private fun String.normalized(): String =
    Normalizer.normalize(trim(), Normalizer.Form.NFD)
        .replace("\\p{M}+".toRegex(), "")
        .lowercase(java.util.Locale.ROOT)

fun filterDestinations(
    destinations: List<Destination>,
    query: String,
    category: Category?
): List<Destination> {
    val normalizedQuery = query.normalized()

    return destinations.filter { destination ->
        val matchesName =
            destination.name.normalized().contains(normalizedQuery)

        val matchesCategory =
            category == null || destination.category == category

        matchesName && matchesCategory
    }
}