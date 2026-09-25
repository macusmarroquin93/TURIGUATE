package com.example.turiguate.model

/**
 * Representa un destino turístico en el catálogo local de TuriGuate.
 */
data class Destination(
    val id: Int,
    val name: String,
    val category: String,
    val department: String,
    val description: String,
    val longDescription: String,
    val imageUrl: String,
    val price: String,
    val rating: Double,
    val bestTimeToVisit: String,
    val activities: List<String>
)
