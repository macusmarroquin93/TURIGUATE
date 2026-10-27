
package com.example.turiguate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItinerarioDao {

    @Insert
    suspend fun insertar(itinerario: ItinerarioEntity): Long

    @Query("SELECT * FROM itinerarios ORDER BY id DESC")
    fun obtenerItinerarios(): Flow<List<ItinerarioEntity>>

    @Query("SELECT * FROM itinerarios WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ItinerarioEntity?

    @Delete
    suspend fun eliminar(itinerario: ItinerarioEntity)
}
