
package com.example.turiguate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turiguate.data.repository.ItinerarioRepository

class ItinerarioViewModelFactory(
    private val repository: ItinerarioRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(
                ItinerarioViewModel::class.java
            )
        ) {
            @Suppress("UNCHECKED_CAST")
            return ItinerarioViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}
