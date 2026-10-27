
package com.example.turiguate.data.repository

import com.example.turiguate.data.local.ActividadEntity
import com.example.turiguate.data.local.ItinerarioDao
import com.example.turiguate.data.local.ItinerarioEntity
import com.example.turiguate.data.local.TuriguateDatabase
import com.example.turiguate.data.local.guardarItinerarioCompleto
import kotlinx.coroutines.flow.Flow

class ItinerarioRepository(
    private val itinerarioDao: ItinerarioDao,
    private val database: TuriguateDatabase
) {

    val itinerarios: Flow<List<ItinerarioEntity>> =
        itinerarioDao.obtenerItinerarios()

    suspend fun guardarItinerario(
        itinerario: ItinerarioEntity
    ) {
        itinerarioDao.insertar(itinerario)
    }

    suspend fun obtenerItinerario(
        id: Int
    ): ItinerarioEntity? {
        return itinerarioDao.obtenerPorId(id)
    }

    suspend fun eliminarItinerario(
        itinerario: ItinerarioEntity
    ) {
        itinerarioDao.eliminar(itinerario)
    }

    suspend fun guardarCompleto(
        itinerario: ItinerarioEntity,
        actividades: List<ActividadEntity>
    ): Long {
        return database.guardarItinerarioCompleto(
            itinerario = itinerario,
            actividades = actividades
        )
    }
}
