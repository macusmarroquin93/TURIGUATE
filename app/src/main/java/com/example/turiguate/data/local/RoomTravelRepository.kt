package com.example.turiguate.data

import android.content.SharedPreferences
import androidx.room.withTransaction
import com.example.turiguate.data.local.TravelDatabase
import com.example.turiguate.data.local.TravelPlanEntity
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomTravelRepository(
    private val database: TravelDatabase,
    private val legacyPreferences: SharedPreferences
) {
    companion object {
        private const val IMPORT_COMPLETE =
            "room_import_complete_v1"
    }

    private val dao = database.planDao()

    // Room emite los planes cuando cambia la tabla.
    val plans: Flow<List<TravelPlan>> =
        dao.observePlans().map { entities ->
            entities.map { entity ->
                TravelPlan(
                    id = entity.id,
                    title = entity.title,
                    days = entity.days,
                    destinationIds = entity.destinationIds
                )
            }
        }

    suspend fun importLegacyPlans() {
        withContext(Dispatchers.IO) {
            if (
                legacyPreferences.getBoolean(
                    IMPORT_COMPLETE,
                    false
                )
            ) {
                return@withContext
            }

            // Lee los planes del almacenamiento anterior.
            val legacyRepository =
                TravelRepository(legacyPreferences)

            val oldPlans = legacyRepository.loadPlans()
            val importTime = System.currentTimeMillis()

            val entities = oldPlans.mapIndexed { index, plan ->
                TravelPlanEntity(
                    id = plan.id,
                    title = plan.title,
                    days = plan.days,
                    destinationIds = plan.destinationIds,
                    createdAt = importTime - index.toLong()
                )
            }

            // La importación se realiza como una sola transacción.
            database.withTransaction {
                dao.importPlans(entities)
            }

            // Se marca después de completar la escritura en Room.
            val marked = legacyPreferences.edit()
                .putBoolean(IMPORT_COMPLETE, true)
                .commit()

            if (!marked) {
                throw IOException(
                    "No fue posible confirmar la importación."
                )
            }
        }
    }

    suspend fun savePlan(plan: TravelPlan) {
        dao.insertPlan(
            TravelPlanEntity(
                id = plan.id,
                title = plan.title,
                days = plan.days,
                destinationIds = plan.destinationIds
            )
        )
    }
}