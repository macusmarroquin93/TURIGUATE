package com.example.turiguate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.turiguate.ui.components.CategoryChip

/**
 * Pantalla Principal: Planifica tu viaje (Lab #1).
 * Se ha mantenido y adaptado para incluir la navegación al Generador.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onGenerateClick: () -> Unit
) {
    var days by remember { mutableIntStateOf(5) }
    var selectedBudget by remember { mutableStateOf("Medio") }
    val selectedInterests = remember { mutableStateListOf("Cultura", "Gastronomía", "Historia") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "TuriGuate",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Planifica tu viaje",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Planifica tu viaje",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dinos qué buscas y crearemos un itinerario a tu medida en segundos.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            // Sección: Días
            Column {
                Text(
                    text = "¿Cuántos días vas a viajar?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                DayStepper(
                    count = days,
                    onIncrement = { days++ },
                    onDecrement = { if (days > 1) days-- }
                )
            }

            // Sección: Presupuesto
            Column {
                Text(
                    text = "Presupuesto estimado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val budgets = listOf("Bajo costo", "Medio", "Alto")
                    budgets.forEach { budget ->
                        CategoryChip(
                            text = budget,
                            isSelected = selectedBudget == budget,
                            onClick = { selectedBudget = budget },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Sección: Intereses
            Column {
                Text(
                    text = "¿Cuáles son tus intereses?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val interests = listOf(
                        "Aventura", "Cultura", "Gastronomía",
                        "Naturaleza", "Historia", "Descanso",
                        "Playa", "Deportes", "Otros"
                    )
                    interests.forEach { interest ->
                        CategoryChip(
                            text = interest,
                            isSelected = selectedInterests.contains(interest),
                            onClick = {
                                if (selectedInterests.contains(interest)) {
                                    selectedInterests.remove(interest)
                                } else {
                                    selectedInterests.add(interest)
                                }
                            }
                        )
                    }
                }
            }

            // Botón de acción: Navega al Generador
            Button(
                onClick = onGenerateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Generar itinerario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun DayStepper(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onDecrement,
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
            ) {
                Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.width(14.dp).height(2.dp).background(LocalContentColor.current))
                }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "días", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            }

            IconButton(
                onClick = onIncrement,
                modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = "Más", tint = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(onGenerateClick = {})
    }
}
