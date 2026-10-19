
package com.example.turiguate.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "actividades",
    foreignKeys = [
        ForeignKey(
            entity = ItinerarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["itinerarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["itinerarioId"])]
)
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itinerarioId: Int,
    val day: Int,
    val destination: String,
    val description: String,
    val restaurant: String,
    val hotel: String,
    val transport: String,
    val price: String,
    val imageUrl: String
)
