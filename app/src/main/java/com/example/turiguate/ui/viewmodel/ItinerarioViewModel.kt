
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


data class DetalleItinerarioUiState(
    val cargando: Boolean = true,
    val actividades: List<ItineraryItem> = emptyList(),
    val error: String? = null
)

class ItinerarioViewModel(
    private val repository: ItinerarioRepository
) : ViewModel() {

    // Observa los itinerarios guardados en Room
    val itinerarios = repository.itinerarios.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun observarActividades(
        itinerarioId: Int
    ): Flow<DetalleItinerarioUiState> {
        return repository.obtenerActividades(itinerarioId)
            .map { guardadas ->
                DetalleItinerarioUiState(
                    cargando = false,
                    actividades = guardadas.map { actividad ->
                        ItineraryItem(
                            id = actividad.id,
                            day = actividad.day,
                            destination = actividad.destination,
                            description = actividad.description,
                            restaurant = actividad.restaurant,
                            hotel = actividad.hotel,
                            transport = actividad.transport,
                            price = actividad.price,
                            imageUrl = actividad.imageUrl
                        )
                    }
                )
            }
            .onStart {
                emit(DetalleItinerarioUiState())
            }
            .catch {
                emit(
                    DetalleItinerarioUiState(
                        cargando = false,
                        error = "No se pudieron cargar las actividades."
                    )
                )
            }
    }

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
