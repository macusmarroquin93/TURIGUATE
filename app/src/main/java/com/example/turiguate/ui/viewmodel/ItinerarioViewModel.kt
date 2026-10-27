
package com.example.turiguate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turiguate.data.local.ActividadEntity
import com.example.turiguate.data.local.ItinerarioEntity
import com.example.turiguate.data.repository.ItinerarioRepository
import com.example.turiguate.model.ItineraryItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItinerarioViewModel(
    private val repository: ItinerarioRepository
) : ViewModel() {

    // Observa los itinerarios guardados en Room
    val itinerarios = repository.itinerarios.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Guarda un itinerario sencillo
    fun guardarItinerario(
        nombre: String,
        destino: String,
        fecha: String,
        presupuesto: Double
    ) {
        viewModelScope.launch {
            val itinerario = ItinerarioEntity(
                nombre = nombre,
                destino = destino,
                fecha = fecha,
                presupuesto = presupuesto
            )

            repository.guardarItinerario(itinerario)
        }
    }

    // Guarda un itinerario completo con sus actividades
    fun guardarItinerarioGenerado(
        nombre: String,
        destino: String,
        fecha: String,
        presupuesto: Double,
        actividades: List<ItineraryItem>,
        onGuardado: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val itinerario = ItinerarioEntity(
                    nombre = nombre,
                    destino = destino,
                    fecha = fecha,
                    presupuesto = presupuesto
                )

                // Convertir los elementos del generador
                // en entidades de la base de datos
                val actividadesEntity = actividades.map { item ->
                    ActividadEntity(
                        itinerarioId = 0,
                        day = item.day,
                        destination = item.destination,
                        description = item.description,
                        restaurant = item.restaurant,
                        hotel = item.hotel,
                        transport = item.transport,
                        price = item.price,
                        imageUrl = item.imageUrl
                    )
                }

                // Guardar el itinerario y todas sus actividades
                // dentro de una transaccion de Room
                repository.guardarCompleto(
                    itinerario = itinerario,
                    actividades = actividadesEntity
                )

                // Notificar que se guardo correctamente
                onGuardado()

            } catch (e: Exception) {
                // Notificar si ocurre algun error
                onError(e)
            }
        }
    }

    // Elimina un itinerario
    fun eliminarItinerario(
        itinerario: ItinerarioEntity
    ) {
        viewModelScope.launch {
            repository.eliminarItinerario(itinerario)
        }
    }
}
