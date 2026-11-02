package com.example.turiguate.model

data class PreferenciasViaje(
    val dias: Int = 5,
    val presupuesto: String = "Medio",
    val intereses: List<String> = listOf(
        "Cultura",
        "Gastronomía",
        "Historia"
    )
)