package com.example.turiguate.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.turiguate.data.Category
import com.example.turiguate.data.LocalCatalog
import com.example.turiguate.data.RoomTravelRepository
import com.example.turiguate.data.TravelPlan
import com.example.turiguate.data.filterDestinations
import com.example.turiguate.data.local.TravelDatabase
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TravelUiState(
    val query: String = "",
    val category: Category? = null,
    val selectedIds: List<String> = emptyList(),
    val planTitle: String = "",
    val days: Int = 3,
    val savedPlans: List<TravelPlan> = emptyList(),
    val loading: Boolean = true,
    val saving: Boolean = false,
    val loadError: String? = null,
    val message: String? = null
) {
    val visibleDestinations
        get() = filterDestinations(
            LocalCatalog.destinations,
            query,
            category
        )

    val selectedDestinations
        get() = selectedIds.mapNotNull { id ->
            LocalCatalog.destinations.find {
                it.id == id
            }
        }
}

class TravelViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RoomTravelRepository(
        database = TravelDatabase.getInstance(application),
        legacyPreferences = application.getSharedPreferences(
            "turiguate_plans",
            Context.MODE_PRIVATE
        )
    )

    private val _state = MutableStateFlow(TravelUiState())

    val state: StateFlow<TravelUiState> =
        _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadPlans()
    }

    fun loadPlans() {
        if (_state.value.saving) return

        // Evita mantener varias observaciones simultáneas.
        loadJob?.cancel()

        _state.update {
            it.copy(
                loading = true,
                loadError = null
            )
        }

        loadJob = viewModelScope.launch {
            try {
                repository.importLegacyPlans()

                // Se mantiene observando los cambios de Room.
                repository.plans.collect { plans ->
                    _state.update {
                        it.copy(
                            savedPlans = plans,
                            loading = false,
                            loadError = null
                        )
                    }
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        loading = false,
                        loadError =
                            "No se pudieron cargar o importar los planes. " +
                                    "Intenta nuevamente."
                    )
                }
            }
        }
    }

    fun changeQuery(query: String) {
        _state.update {
            it.copy(query = query)
        }
    }

    fun changeCategory(category: Category?) {
        _state.update {
            it.copy(category = category)
        }
    }

    fun clearFilters() {
        _state.update {
            it.copy(
                query = "",
                category = null
            )
        }
    }

    fun toggleDestination(id: String) {
        if (_state.value.saving) return

        if (
            LocalCatalog.destinations.none {
                it.id == id
            }
        ) {
            return
        }

        _state.update { current ->
            val selected =
                if (id in current.selectedIds) {
                    current.selectedIds - id
                } else {
                    current.selectedIds + id
                }

            current.copy(
                selectedIds = selected,
                message = null
            )
        }
    }

    fun changeTitle(title: String) {
        if (_state.value.saving) return

        _state.update {
            it.copy(
                planTitle = title.take(80),
                message = null
            )
        }
    }

    fun changeDays(days: Int) {
        if (_state.value.saving) return

        _state.update {
            it.copy(
                days = days.coerceIn(1, 30),
                message = null
            )
        }
    }

    fun savePlan() {
        val current = _state.value

        if (
            current.loading ||
            current.saving ||
            current.loadError != null
        ) {
            return
        }

        val plan = try {
            TravelPlan(
                id = UUID.randomUUID().toString(),
                title = current.planTitle.trim(),
                days = current.days,
                destinationIds =
                    current.selectedIds.toList()
            )
        } catch (error: IllegalArgumentException) {
            _state.update {
                it.copy(message = error.message)
            }
            return
        }

        _state.update {
            it.copy(
                saving = true,
                message = null
            )
        }

        viewModelScope.launch {
            try {
                repository.savePlan(plan)

                // La lista de planes se actualiza desde el Flow de Room.
                _state.update {
                    it.copy(
                        saving = false,
                        selectedIds = emptyList(),
                        planTitle = "",
                        message =
                            "Plan guardado en este dispositivo."
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _state.update {
                    it.copy(
                        saving = false,
                        message =
                            "No se pudo guardar. " +
                                    "Tu selección se conserva; " +
                                    "intenta nuevamente."
                    )
                }
            }
        }
    }
}