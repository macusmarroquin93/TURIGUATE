package com.example.turiguate.data

import com.example.turiguate.R
import com.example.turiguate.model.Destination

/**
 * Repositorio de datos locales para el catálogo turístico de TuriGuate.
 * Precios ajustados en Quetzales (Q) basados en tarifas reales estimadas de Guatemala.
 */
object LocalCatalogRepository {

    val categories = listOf(
        "Todas",
        "Cultura",
        "Naturaleza",
        "Aventura",
        "Gastronomía",
        "Historia",
        "Playa",
    )

    private val destinations = listOf(
        Destination(
            id = 1,
            name = "Antigua Guatemala",
            category = "Cultura",
            department = "Sacatepéquez",
            description = "Ciudad colonial rodeada de volcanes con arquitectura del siglo XVI.",
            longDescription = "Antigua Guatemala es famosa por sus calles empedradas, iglesias coloniales restauradas y ruinas de monumentos religiosos. Es Patrimonio de la Humanidad por la UNESCO y ofrece experiencias únicas en cata de café artesanal, alta gastronomía y vistas impresionantes hacia los volcanes de Agua, Fuego y Acatenango.",
            imageRes = R.drawable.antigua,
            price = "Q 350.00",
            rating = 4.9,
            bestTimeToVisit = "Noviembre a Abril",
            activities = listOf("Recorrido colonial", "Cata de café", "Subida al Cerro de la Cruz", "Visita a ruinas e iglesias")
        ),
        Destination(
            id = 2,
            name = "Parque Nacional Tikal",
            category = "Historia",
            department = "Petén",
            description = "Antigua metrópoli maya inmersa en una exuberante selva tropical.",
            longDescription = "Tikal es una de las joyas arqueológicas más importantes de la civilización maya. Sus imponentes pirámides se alzan sobre la copa de los árboles en la Reserva de la Biosfera Maya. Escucha el aullido de los monos aulladores al amanecer mientras contemplas el Templo del Gran Jaguar.",
            imageRes = R.drawable.tikal,
            price = "Q 450.00",
            rating = 5.0,
            bestTimeToVisit = "Diciembre a Marzo",
            activities = listOf("Tour al amanecer", "Avistamiento de fauna", "Exploración de templos", "Museo de la cerámica maya")
        ),
        Destination(
            id = 3,
            name = "Semuc Champey",
            category = "Naturaleza",
            department = "Alta Verapaz",
            description = "Monumental puente natural de piedra con pozas de agua turquesa.",
            longDescription = "Enclavado en la densa selva de Cobán, Semuc Champey consiste en una serie de pozas naturales de cristalina agua turquesa alimentadas por manantiales subterráneos. Debajo de las pozas fluye el cauce del Río Cahabón. Un mirador espectacular ofrece una panorámica inolvidable.",
            imageRes = R.drawable.semuc_champey,
            price = "Q 300.00",
            rating = 4.8,
            bestTimeToVisit = "Enero a Mayo",
            activities = listOf("Nado en pozas", "Trekking al Mirador", "Tubing en río Cahabón", "Cuevas de K'an Ba")
        ),
        Destination(
            id = 4,
            name = "Lago de Atitlán",
            category = "Naturaleza",
            department = "Sololá",
            description = "Considerado uno de los lagos más bellos del mundo, rodeado de volcanes.",
            longDescription = "El Lago de Atitlán combina paisajes naturales impresionantes con una vibrante cultura maya viva. A sus orillas descansan pintorescos pueblos como San Juan La Laguna, famoso por sus textiles naturales y galerías de arte, y Panajachel, el portal de entrada al lago.",
            imageRes = R.drawable.atitlan,
            price = "Q 350.00",
            rating = 4.9,
            bestTimeToVisit = "Noviembre a Mayo",
            activities = listOf("Paseo en lancha", "Tour de teñido textil", "Kayak y Paddleboard", "Mirador Rostro Maya")
        ),
        Destination(
            id = 5,
            name = "Volcán de Acatenango",
            category = "Aventura",
            department = "Chimaltenango",
            description = "Caminata desafiante con impresionantes vistas de las erupciones del Volcán de Fuego.",
            longDescription = "Una de las aventuras de trekking más populares del mundo. Subir el Acatenango requiere resistencia física, pero recompensa a los viajeros con un campamento nocturno con vista directa a la actividad eruptiva y explosiones de lava del vecino Volcán de Fuego.",
            imageRes = R.drawable.pacaya,
            price = "Q 550.00",
            rating = 4.9,
            bestTimeToVisit = "Noviembre a Abril",
            activities = listOf("Trekking de alta montaña", "Campamento nocturno", "Fotografía nocturna de lava", "Amanecer en la cumbre")
        ),
        Destination(
            id = 6,
            name = "Río Dulce y Livingston",
            category = "Aventura",
            department = "Izabal",
            description = "Navegación por cañones selváticos hasta la vibrante cultura garífuna.",
            longDescription = "Río Dulce conecta el Lago de Izabal con el Mar Caribe a través de un espectacular cañón cubierto de vegetación tropical. El recorrido conduce a Livingston, un pueblo único en Guatemala caracterizado por su rica cultura garífuna, música de tambores y deliciosa gastronomía marina.",
            imageRes = R.drawable.rio_dulce,
            price = "Q 400.00",
            rating = 4.7,
            bestTimeToVisit = "Febrero a Mayo",
            activities = listOf("Recorrido en lancha", "Visita al Castillo de San Felipe", "Degustación de Tapado", "Aguas termales")
        ),
        Destination(
            id = 7,
            name = "Playa Monterrico",
            category = "Playa",
            department = "Santa Rosa",
            description = "Costa del Pacífico con pacíficas playas de arena volcánica negra.",
            longDescription = "Monterrico es una reserva natural en el Pacífico guatemalteco famosa por su biodiversidad en manglares y la conservación de tortugas marinas. Sus atardeceres dorados y olas ideales para el descanso la convierten en el destino costero favorito del país.",
            imageRes = R.drawable.monterrico,
            price = "Q 350.00",
            rating = 4.6,
            bestTimeToVisit = "Todo el año",
            activities = listOf("Liberación de tortugas", "Paseo en lancha por manglares", "Surf", "Gastronomía de mariscos")
        ),
        Destination(
            id = 8,
            name = "IRTRA Retalhuleu",
            category = "Aventura",
            department = "Retalhuleu",
            description = "Complejo de parques temáticos y acuáticos de nivel internacional.",
            longDescription = "Conocido como el parque de diversiones más grande de Centroamérica, incluye Xetulul (temático) y Xocomil (acuático), además de resorts de descanso como Hostales del IRTRA y Aldea de la Selva. Ideal para viajes familiares y diversión sin fin.",
            imageRes = R.drawable.irtra,
            price = "Q 650.00",
            rating = 4.9,
            bestTimeToVisit = "Todo el año",
            activities = listOf("Juegos mecánicos y montañas rusas", "Toboganes acuáticos", "Espectáculos en vivo", "Hospedaje temático")
        ),
        Destination(
            id = 9,
            name = "Ruta Gastronómica de Salcajá y Xela",
            category = "Gastronomía",
            department = "Quetzaltenango",
            description = "Sabores autóctonos del altiplano: pepián, caldo de res y licor de Caldo de Frutas.",
            longDescription = "Quetzaltenango (Xela) y sus municipios vecinos albergan tradiciones culinarias ancestrales. Explora los mercados locales, prueba el tradicional pepián servido en escudillas de barro, los panes artesanales de Xela y el emblemático Caldo de Frutas de Salcajá.",
            imageRes = R.drawable.xela,
            price = "Q 250.00",
            rating = 4.7,
            bestTimeToVisit = "Septiembre a Febrero",
            activities = listOf("Tour gastronómico", "Visita al Teatro Municipal", "Caminata en Fuentes Georginas", "Mercado artesanal")
        )
    )

    @Suppress("unused")
    fun getDestinations(): List<Destination> = destinations

    fun getDestinationsByCategory(category: String): List<Destination> {
        if (category == "Todas") return getDestinations()
        return getDestinations().filter { it.category.equals(category, ignoreCase = true) }
    }

    fun getDestinationById(id: Int): Destination? {
        return destinations.find { it.id == id }
    }

    fun searchDestinations(query: String, selectedCategory: String): List<Destination> {
        var result = if (selectedCategory == "Todas") destinations else getDestinationsByCategory(selectedCategory)
        if (query.isNotBlank()) {
            result = result.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.department.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
        return result
    }
}
