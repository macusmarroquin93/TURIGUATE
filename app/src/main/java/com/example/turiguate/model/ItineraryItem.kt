package com.example.turiguate.model

import androidx.annotation.DrawableRes

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
    @get:DrawableRes val imageRes: Int
)
