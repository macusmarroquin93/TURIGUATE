
package com.example.turiguate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {

    @Insert
    suspend fun insertarActividades(
        actividades: List<ActividadEntity>
    )

    @Query(
        """
        SELECT * FROM actividades
        WHERE itinerarioId = :itinerarioId
        ORDER BY day ASC, id ASC
        """
    )
    fun obtenerActividades(
        itinerarioId: Int
    ): Flow<List<ActividadEntity>>
}
