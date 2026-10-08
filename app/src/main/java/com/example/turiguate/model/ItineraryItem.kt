package com.example.turiguate.model

/**
 * Data class inmutable que representa un destino en el itinerario.
 * Utiliza 'val' para todas las propiedades para asegurar la inmutabilidad.
 */
data class ItineraryItem(
    val id: Int,
    val day: Int,
    val destination: String,
    val description: String,
    val restaurant: String,
    val hotel: String,
    val transport: String,
    val price: String,
    val imageUrl: String
)
