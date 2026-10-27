package com.example.turiguate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.turiguate.PlanScreen
import com.example.turiguate.R
import com.example.turiguate.data.Category
import com.example.turiguate.data.Destination
import com.example.turiguate.data.LocalCatalog
import com.example.turiguate.ui.TravelUiState
import com.example.turiguate.ui.TravelViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    model: TravelViewModel = viewModel()
) {
    val state by model.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: "catalog"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TuriGuate") },
                navigationIcon = {
                    if (route != "catalog") {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = route == "catalog",
                    onClick = {
                        navController.navigate("catalog") {
                            popUpTo("catalog") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    label = { Text("Explorar") }
                )

                NavigationBarItem(
                    selected = route == "plan",
                    onClick = {
                        navController.navigate("plan") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.List,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text("Mi plan (${state.selectedIds.size})")
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            state.loadError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

                TextButton(
                    onClick = model::loadPlans,
                    enabled = !state.loading
                ) {
                    Text("Reintentar lectura")
                }
            }

            state.message?.let { message ->
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp)
                )
            }

            NavHost(
                navController = navController,
                startDestination = "catalog",
                modifier = Modifier.weight(1f)
            ) {
                composable("catalog") {
                    CatalogScreen(
                        state = state,
                        onQueryChange = model::changeQuery,
                        onCategoryChange = model::changeCategory,
                        onClearFilters = model::clearFilters,
                        onDestinationClick = { id ->
                            navController.navigate("detail/$id")
                        }
                    )
                }

                composable("detail/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")

                    val destination = LocalCatalog.destinations.find {
                        it.id == id
                    }

                    if (destination == null) {
                        Text(
                            "Destino no disponible.",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        DestinationDetailScreen(
                            destination = destination,
                            selected = destination.id in state.selectedIds,
                            enabled = !state.saving,
                            onToggleSelection = {
                                model.toggleDestination(destination.id)
                            },
                            onOpenPlan = {
                                navController.navigate("plan") {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }

                composable("plan") {
                    PlanScreen(
                        state = state,
                        onTitleChange = model::changeTitle,
                        onDaysChange = model::changeDays,
                        onRemoveDestination = model::toggleDestination,
                        onSave = model::savePlan,
                        onExplore = {
                            navController.navigate("catalog") {
                                popUpTo("catalog") { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogScreen(
    state: TravelUiState,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (Category?) -> Unit,
    onClearFilters: () -> Unit,
    onDestinationClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Descubre Guatemala",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                label = { Text("Buscar por nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.category == null,
                        onClick = { onCategoryChange(null) },
                        label = { Text("Todas") }
                    )
                }

                items(Category.values().toList()) { category ->
                    FilterChip(
                        selected = state.category == category,
                        onClick = { onCategoryChange(category) },
                        label = { Text(category.label) }
                    )
                }
            }
        }

        item {
            Text("${state.visibleDestinations.size} destinos encontrados")
        }

        if (state.visibleDestinations.isEmpty()) {
            item {
                Column {
                    Text("No hay destinos con esta búsqueda y categoría.")

                    TextButton(onClick = onClearFilters) {
                        Text("Limpiar filtros")
                    }
                }
            }
        }

        items(
            items = state.visibleDestinations,
            key = { it.id }
        ) { destination ->
            Card(
                onClick = { onDestinationClick(destination.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                DestinationIllustration(destination)

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        destination.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        "${destination.department} · ${destination.category.label}"
                    )

                    if (destination.id in state.selectedIds) {
                        Text(
                            "Incluido en tu plan",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text("Ver detalle")
                }
            }
        }
    }
}

@Composable
private fun DestinationDetailScreen(
    destination: Destination,
    selected: Boolean,
    enabled: Boolean,
    onToggleSelection: () -> Unit,
    onOpenPlan: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { DestinationIllustration(destination) }

        item {
            Text(
                destination.name,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text("${destination.department} · ${destination.category.label}")
        }

        item { Text(destination.description) }

        item {
            Button(
                onClick = onToggleSelection,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (selected) "Quitar de mi plan"
                    else "Agregar a mi plan"
                )
            }
        }

        item {
            OutlinedButton(
                onClick = onOpenPlan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Revisar mi plan")
            }
        }
    }
}

@Composable
private fun DestinationIllustration(
    destination: Destination
) {
    val fallbackImage =
        painterResource(R.drawable.destination_landscape)

    AsyncImage(
        model = "file:///android_asset/destinations/${destination.id}.jpg",
        contentDescription = "Fotografía de ${destination.name}",
        placeholder = fallbackImage,
        error = fallbackImage,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    )
}