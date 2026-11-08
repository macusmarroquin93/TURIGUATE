package com.example.turiguate.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.turiguate.R
import com.example.turiguate.model.ItineraryItem
import com.example.turiguate.model.formatearQuetzales
import com.example.turiguate.model.obtenerCostosParaMostrar
import kotlin.math.abs

@Composable
fun ItineraryCard(
    item: ItineraryItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val costos = obtenerCostosParaMostrar(item)

    val precioGuardado = item.price
        .replace("Q", "")
        .replace(",", "")
        .trim()
        .toDoubleOrNull()

    // Evita mostrar un desglose nuevo junto a un precio
    // antiguo guardado que no coincide con ese desglose.
    val mostrarDesglose = costos != null &&
            precioGuardado != null &&
            abs(costos.total - precioGuardado) < 0.005

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(item.destination)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme
                .surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Día ${item.day}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            AsyncImage(
                model = item.imageUrl,
                contentDescription = "Imagen de ${item.destination}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(
                    R.drawable.destination_landscape
                ),
                error = painterResource(
                    R.drawable.destination_landscape
                )
            )

            Text(
                text = item.destination,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            InfoRow(
                icon = Icons.Default.Info,
                label = "Restaurante: ",
                value = item.restaurant
            )

            InfoRow(
                icon = Icons.Default.Info,
                label = "Hospedaje: ",
                value = item.hotel
            )

            InfoRow(
                icon = Icons.Default.Info,
                label = "Traslado: ",
                value = item.transport
            )

            HorizontalDivider()

            if (mostrarDesglose && costos != null) {
                Text(
                    text = "Costos por persona al día",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                CategoriaCosto(
                    titulo = "Comida",
                    monto = costos.food
                )

                CategoriaCosto(
                    titulo = "Estadía · una noche",
                    monto = costos.accommodation
                )

                CategoriaCosto(
                    titulo = "Actividades",
                    monto = costos.activities
                )

                HorizontalDivider()

                CategoriaCosto(
                    titulo = "Total diario",
                    monto = costos.total,
                    destacado = true
                )

                Text(
                    text = "Estimaciones de ejemplo. " +
                            "No incluyen transporte ni representan " +
                            "cotizaciones de los establecimientos mencionados.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = item.price,
                    modifier = Modifier.align(Alignment.End),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Este itinerario no tiene un desglose " +
                            "compatible con los costos actuales. " +
                            "Genera uno nuevo para verlo por categorías.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CategoriaCosto(
    titulo: String,
    monto: Double,
    destacado: Boolean = false
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = if (destacado) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (destacado) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )

            Text(
                text = formatearQuetzales(monto),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (destacado) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.secondary
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}