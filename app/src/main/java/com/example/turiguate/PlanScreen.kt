package com.example.turiguate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.turiguate.data.LocalCatalog
import com.example.turiguate.ui.TravelUiState

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun PlanScreen(
    state: TravelUiState,
    onTitleChange: (String) -> Unit,
    onDaysChange: (Int) -> Unit,
    onRemoveDestination: (String) -> Unit,
    onSave: () -> Unit,
    onExplore: () -> Unit
) {
    var selectedBudget by rememberSaveable {
        mutableStateOf("Medio")
    }

    var selectedInterests by rememberSaveable {
        mutableStateOf(listOf("Cultura", "Naturaleza"))
    }

    val budgets = listOf("Bajo costo", "Medio", "Alto")

    val interests = listOf(
        "Aventura",
        "Cultura",
        "Gastronomía",
        "Naturaleza",
        "Historia",
        "Descanso",
        "Playa",
        "Deportes",
        "Otros"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Presentación
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Planifica tu viaje",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Organiza tu próxima aventura por Guatemala.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Nombre del plan
        item {
            OutlinedTextField(
                value = state.planTitle,
                onValueChange = onTitleChange,
                enabled = !state.saving,
                label = { Text("Nombre de tu viaje") },
                placeholder = {
                    Text("Ejemplo: Vacaciones en Guatemala")
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Duración
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¿Cuántos días vas a viajar?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                        .copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onDaysChange(state.days - 1)
                            },
                            enabled = state.days > 1 && !state.saving,
                            contentPadding = PaddingValues(12.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "−",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Text(
                            text = "${state.days} días",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = {
                                onDaysChange(state.days + 1)
                            },
                            enabled = state.days < 30 && !state.saving,
                            contentPadding = PaddingValues(12.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        }

        // Presupuesto
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Presupuesto estimado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    budgets.forEach { budget ->
                        FilterChip(
                            selected = selectedBudget == budget,
                            onClick = {
                                selectedBudget = budget
                            },
                            enabled = !state.saving,
                            label = { Text(budget) },
                            shape = RoundedCornerShape(24.dp)
                        )
                    }
                }
            }
        }

        // Intereses
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¿Cuáles son tus intereses?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    interests.forEach { interest ->
                        FilterChip(
                            selected = interest in selectedInterests,
                            onClick = {
                                selectedInterests =
                                    if (interest in selectedInterests) {
                                        selectedInterests - interest
                                    } else {
                                        selectedInterests + interest
                                    }
                            },
                            enabled = !state.saving,
                            label = { Text(interest) },
                            shape = RoundedCornerShape(24.dp)
                        )
                    }
                }
            }
        }

        // Destinos que vienen del catálogo
        item {
            Column {
                Text(
                    text = "Destinos de tu viaje",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onExplore) {
                    Text("Explorar y agregar destinos")
                }

                if (state.selectedDestinations.isEmpty()) {
                    Text(
                        text = "Elige al menos un destino para crear tu plan.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(
            items = state.selectedDestinations,
            key = { "selected_${it.id}" }
        ) { destination ->
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = destination.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = destination.department,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    TextButton(
                        onClick = {
                            onRemoveDestination(destination.id)
                        },
                        enabled = !state.saving
                    ) {
                        Text("Quitar del plan")
                    }
                }
            }
        }

        // Crear y guardar el plan básico
        item {
            Button(
                onClick = onSave,
                enabled = !state.loading &&
                        !state.saving &&
                        state.loadError == null &&
                        state.planTitle.isNotBlank() &&
                        state.selectedIds.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (state.saving) {
                        "Guardando..."
                    } else {
                        "Crear y guardar plan"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Planes previamente guardados
        item {
            HorizontalDivider()
        }

        item {
            Text(
                text = "Mis planes guardados",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        if (
            !state.loading &&
            state.loadError == null &&
            state.savedPlans.isEmpty()
        ) {
            item {
                Text(
                    text = "Tus planes aparecerán aquí después de guardarlos.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(
            items = state.savedPlans,
            key = { "saved_${it.id}" }
        ) { plan ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = plan.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${plan.days} días · " +
                                "${plan.destinationIds.size} destinos",
                        color = MaterialTheme.colorScheme.primary
                    )

                    plan.destinationIds.forEachIndexed { index, id ->
                        val destination =
                            LocalCatalog.destinations.find {
                                it.id == id
                            }

                        Text(
                            text = "${index + 1}. " +
                                    (destination?.name
                                        ?: "Destino no disponible")
                        )
                    }
                }
            }
        }
    }
}