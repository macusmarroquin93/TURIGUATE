package com.example.turiguate.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.turiguate.TuriguateApplication
import com.example.turiguate.ui.components.ItineraryCard
import com.example.turiguate.ui.viewmodel.DetalleItinerarioUiState
import com.example.turiguate.ui.viewmodel.ItinerarioViewModel
import com.example.turiguate.ui.viewmodel.ItinerarioViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleItinerarioScreen(
    itinerarioId: Int,
    nombre: String,
    onBackClick: () -> Unit
) {
    val application = LocalContext.current.applicationContext
            as TuriguateApplication

    val modelo: ItinerarioViewModel = viewModel(
        factory = ItinerarioViewModelFactory(
            application.itinerarioRepository
        )
    )

    var reintento by remember(itinerarioId) {
        mutableIntStateOf(0)
    }

    val flujo = remember(itinerarioId, modelo, reintento) {
        modelo.observarActividades(itinerarioId)
    }

    val estado by flujo.collectAsStateWithLifecycle(
        initialValue = DetalleItinerarioUiState()
    )

    BackHandler(onBack = onBackClick)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = nombre,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a mis itinerarios"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            estado.cargando -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            estado.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = estado.error
                            ?: "No se pudo cargar el itinerario.",
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { reintento++ }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            estado.actividades.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Este itinerario no tiene actividades guardadas."
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = estado.actividades,
                        key = { actividad -> actividad.id }
                    ) { actividad ->
                        ItineraryCard(
                            item = actividad,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}