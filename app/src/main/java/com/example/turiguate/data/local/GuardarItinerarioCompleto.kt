
package com.example.turiguate.data.local

import androidx.room.withTransaction

suspend fun TuriguateDatabase.guardarItinerarioCompleto(
    itinerario: ItinerarioEntity,
    actividades: List<ActividadEntity>
): Long {
    return withTransaction {

        val itinerarioId = itinerarioDao()
            .insertar(itinerario)

        val actividadesConId = actividades.map {
            it.copy(itinerarioId = itinerarioId.toInt())
        }

        actividadDao().insertarActividades(
            actividadesConId
        )

        itinerarioId
    }
}
