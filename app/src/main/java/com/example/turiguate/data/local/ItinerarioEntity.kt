
package com.example.turiguate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itinerarios")
data class ItinerarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val destino: String,
    val fecha: String,
    val presupuesto: Double
)
