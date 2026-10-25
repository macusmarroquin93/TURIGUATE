package com.example.turiguate

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.turiguate.data.TravelPlan
import com.example.turiguate.data.TravelRepository
import java.util.UUID
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TravelRepositoryTest {

    @Test
    fun savedPlanCanBeLoadedByANewRepository() {
        val context =
            InstrumentationRegistry.getInstrumentation().targetContext

        val preferences = context.getSharedPreferences(
            "test_plans_${UUID.randomUUID()}",
            Context.MODE_PRIVATE
        )

        try {
            val plan = TravelPlan(
                id = "plan-1",
                title = "Viaje de prueba",
                days = 3,
                destinationIds = listOf("antigua", "atitlan")
            )

            val firstRepository = TravelRepository(preferences)
            firstRepository.savePlan(plan)

            val reopenedRepository = TravelRepository(preferences)

            assertEquals(
                listOf(plan),
                reopenedRepository.loadPlans()
            )
        } finally {
            preferences.edit().clear().commit()
        }
    }

    @Test
    fun damagedStorageIsNotSilentlyOverwritten() {
        val context =
            InstrumentationRegistry.getInstrumentation().targetContext

        val preferences = context.getSharedPreferences(
            "test_corrupt_plans_${UUID.randomUUID()}",
            Context.MODE_PRIVATE
        )

        try {
            val damagedContent = "contenido inválido"

            assertTrue(
                preferences.edit()
                    .putString("saved_plans_v1", damagedContent)
                    .commit()
            )

            val repository = TravelRepository(preferences)

            val result = runCatching {
                repository.savePlan(
                    TravelPlan(
                        id = "new-plan",
                        title = "Nuevo viaje",
                        days = 2,
                        destinationIds = listOf("antigua")
                    )
                )
            }

            assertTrue(result.isFailure)

            assertEquals(
                damagedContent,
                preferences.getString("saved_plans_v1", null)
            )
        } finally {
            preferences.edit().clear().commit()
        }
    }
}