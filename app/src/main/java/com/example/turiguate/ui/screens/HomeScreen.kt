package com.example.turiguate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.turiguate.R
import com.example.turiguate.ui.components.CategoryChip

// Icono personalizado de Sol para Tema Claro
val SunIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Sun",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = SolidColor(Color(0xFFFFC107))) {
        moveTo(12f, 7f)
        curveTo(9.24f, 7f, 7f, 9.24f, 7f, 12f)
        curveTo(7f, 14.76f, 9.24f, 17f, 12f, 17f)
        curveTo(14.76f, 17f, 17f, 14.76f, 17f, 12f)
        curveTo(17f, 9.24f, 14.76f, 7f, 12f, 7f)
        close()
        moveTo(12f, 2f)
        lineTo(12f, 4f)
        moveTo(12f, 20f)
        lineTo(12f, 22f)
        moveTo(4.22f, 4.22f)
        lineTo(5.64f, 5.64f)
        moveTo(18.36f, 18.36f)
        lineTo(19.78f, 19.78f)
        moveTo(2f, 12f)
        lineTo(4f, 12f)
        moveTo(20f, 12f)
        lineTo(22f, 12f)
        moveTo(4.22f, 19.78f)
        lineTo(5.64f, 18.36f)
        moveTo(18.36f, 5.64f)
        lineTo(19.78f, 4.22f)
    }.build()
}

// Icono personalizado de Luna para Tema Oscuro
val MoonIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Moon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = SolidColor(Color(0xFF90CAF9))) {
        moveTo(12.3f, 2f)
        curveTo(6.97f, 2f, 2.65f, 6.32f, 2.65f, 11.65f)
        curveTo(2.65f, 17f, 6.97f, 21.3f, 12.3f, 21.3f)
        curveTo(15.93f, 21.3f, 19.1f, 19.3f, 20.8f, 16.3f)
        curveTo(14.5f, 16.3f, 9.7f, 11.5f, 9.7f, 5.2f)
        curveTo(9.7f, 4.05f, 9.92f, 2.98f, 10.33f, 2f)
        curveTo(10.97f, 2f, 11.63f, 2f, 12.3f, 2f)
        close()
    }.build()
}

/**
 * Pantalla Principal: Planifica tu viaje.
 * Incluye acceso al Generador de Itinerarios, Catálogo de Destinos y alternador de Tema Claro/Oscuro.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onGenerateClick: () -> Unit,
    onCatalogClick: () -> Unit = {},
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {}
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
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) SunIcon else MoonIcon,
                            contentDescription = if (isDarkTheme) "Cambiar a Tema Claro" else "Cambiar a Tema Oscuro"
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    text = stringResource(R.string.cu_les_son_tus_intereses),
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

            // Acciones principales
            Column(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón: Generar itinerario
                Button(
                    onClick = onGenerateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
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
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }

                // Botón: Explorar Catálogo
                OutlinedButton(
                    onClick = onCatalogClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Explorar Catálogo Turístico",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
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
                modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            ) {
                Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .width(14.dp)
                            .height(2.dp)
                            .background(LocalContentColor.current)
                    )
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
                Text(
                    text = "días",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(
                onClick = onIncrement,
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Más", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(onGenerateClick = {}, onCatalogClick = {})
    }
}
