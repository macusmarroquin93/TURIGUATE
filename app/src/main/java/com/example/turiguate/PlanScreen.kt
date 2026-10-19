
package com.example.turiguate

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.turiguate.ui.viewmodel.ItinerarioViewModel
import com.example.turiguate.ui.viewmodel.ItinerarioViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val application =
        context.applicationContext as TuriguateApplication

    val itinerarioViewModel: ItinerarioViewModel = viewModel(
        factory = ItinerarioViewModelFactory(
            application.itinerarioRepository
        )
    )

    val itinerarios by itinerarioViewModel.itinerarios
        .collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mis itinerarios")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar a inicio"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (itinerarios.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Todavía no tienes itinerarios guardados."
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = itinerarios,
                    key = { itinerario -> itinerario.id }
                ) { itinerario ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = itinerario.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "Destino: ${itinerario.destino}"
                            )

                            Text(
                                text = "Fecha: ${itinerario.fecha}"
                            )

                            Text(
                                text = "Presupuesto: Q ${itinerario.presupuesto}"
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            TextButton(
                                onClick = {
                                    itinerarioViewModel.eliminarItinerario(
                                        itinerario
                                    )
                                }
                            ) {
                                Text("Eliminar itinerario")
                            }
                        }
                    }
                }
            }
        }
    }
}
