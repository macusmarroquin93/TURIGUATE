package com.example.turiguate

import androidx.lifecycle.ViewModelStore
import com.example.turiguate.data.local.ActividadEntity
import com.example.turiguate.data.local.ItinerarioEntity
import com.example.turiguate.data.repository.ItinerarioRepository
import com.example.turiguate.model.ItineraryItem
import com.example.turiguate.ui.viewmodel.ItinerarioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class ItinerarioViewModelTest {

    private lateinit var repository: ItinerarioRepository
    private val store = ViewModelStore()

    private val actividad = ItineraryItem(
        id = 10,
        day = 2,
        destination = "Antigua Guatemala",
        description = "Recorrido por el centro histórico.",
        restaurant = "Restaurante de prueba",
        hotel = "Hotel de prueba",
        transport = "Transporte de prueba",
        price = "Q 450.00",
        imageUrl = "file:///android_asset/destinations/antigua.jpg"
    )

    private val actividadParaGuardar = ActividadEntity(
        id = 0,
        itinerarioId = 0,
        day = 2,
        destination = "Antigua Guatemala",
        description = "Recorrido por el centro histórico.",
        restaurant = "Restaurante de prueba",
        hotel = "Hotel de prueba",
        transport = "Transporte de prueba",
        price = "Q 450.00",
        imageUrl = "file:///android_asset/destinations/antigua.jpg"
    )

    private val plan = ItinerarioEntity(
        nombre = "Viaje de prueba",
        destino = "Guatemala",
        fecha = "2026-11-02",
        presupuesto = 450.0
    )

    @Before
    fun preparar() {
        Dispatchers.setMain(StandardTestDispatcher())

        repository = Mockito.mock(
            ItinerarioRepository::class.java
        )

        Mockito.`when`(repository.itinerarios)
            .thenReturn(flowOf(emptyList()))
    }

    @After
    fun limpiar() {
        store.clear()
        Dispatchers.resetMain()
    }

    private fun crearViewModel(): ItinerarioViewModel {
        val modelo = ItinerarioViewModel(repository)
        store.put("modelo", modelo)
        return modelo
    }

    @Test
    fun guardarConservaLosDatosYNotificaExito() = runTest {
        Mockito.doReturn(7L)
            .`when`(repository)
            .guardarCompleto(
                plan,
                listOf(actividadParaGuardar)
            )

        val modelo = crearViewModel()
        var guardado = false
        var error: Throwable? = null

        modelo.guardarItinerarioGenerado(
            nombre = plan.nombre,
            destino = plan.destino,
            fecha = plan.fecha,
            presupuesto = plan.presupuesto,
            actividades = listOf(actividad),
            onGuardado = {
                guardado = true
            },
            onError = {
                error = it
            }
        )

        advanceUntilIdle()

        Mockito.verify(repository, Mockito.times(1))
            .guardarCompleto(
                plan,
                listOf(actividadParaGuardar)
            )

        assertTrue(guardado)
        assertNull(error)
    }

    @Test
    fun falloAlGuardarNotificaErrorSinAnunciarExito() = runTest {
        val fallo = IllegalStateException(
            "Fallo de almacenamiento de prueba"
        )

        Mockito.doThrow(fallo)
            .`when`(repository)
            .guardarCompleto(
                plan,
                listOf(actividadParaGuardar)
            )

        val modelo = crearViewModel()
        var guardado = false
        var errorRecibido: Throwable? = null

        modelo.guardarItinerarioGenerado(
            nombre = plan.nombre,
            destino = plan.destino,
            fecha = plan.fecha,
            presupuesto = plan.presupuesto,
            actividades = listOf(actividad),
            onGuardado = {
                guardado = true
            },
            onError = {
                errorRecibido = it
            }
        )

        advanceUntilIdle()

        assertFalse(guardado)
        assertSame(fallo, errorRecibido)
    }

    @Test
    fun detalleCargaLasActividadesDelPlanSeleccionado() = runTest {
        val guardada = actividadParaGuardar.copy(
            id = 101,
            itinerarioId = 7
        )

        Mockito.`when`(repository.obtenerActividades(7))
            .thenReturn(flowOf(listOf(guardada)))

        val modelo = crearViewModel()
        val estados = modelo.observarActividades(7).toList()

        assertTrue(estados.first().cargando)

        val resultado = estados.last()

        assertFalse(resultado.cargando)
        assertNull(resultado.error)
        assertEquals(
            listOf(actividad.copy(id = 101)),
            resultado.actividades
        )

        Mockito.verify(repository).obtenerActividades(7)
    }

    @Test
    fun detalleSinActividadesTerminaLaCargaSinError() = runTest {
        Mockito.`when`(repository.obtenerActividades(7))
            .thenReturn(flowOf(emptyList()))

        val modelo = crearViewModel()
        val estados = modelo.observarActividades(7).toList()

        assertTrue(estados.first().cargando)

        val resultado = estados.last()

        assertFalse(resultado.cargando)
        assertTrue(resultado.actividades.isEmpty())
        assertNull(resultado.error)
    }

    @Test
    fun falloAlConsultarDetalleMuestraError() = runTest {
        Mockito.`when`(repository.obtenerActividades(7))
            .thenReturn(
                flow<List<ActividadEntity>> {
                    throw IllegalStateException(
                        "Fallo de lectura de prueba"
                    )
                }
            )

        val modelo = crearViewModel()
        val estados = modelo.observarActividades(7).toList()

        assertTrue(estados.first().cargando)

        val resultado = estados.last()

        assertFalse(resultado.cargando)
        assertTrue(resultado.actividades.isEmpty())
        assertFalse(resultado.error.isNullOrBlank())
    }
}