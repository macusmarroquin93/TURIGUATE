package com.example.turiguate.data

import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class TravelRepository(
    private val preferences: SharedPreferences
) {
    companion object {
        private const val PLANS_KEY = "saved_plans_v1"
    }

    fun loadPlans(): List<TravelPlan> {
        val stored = preferences.getString(PLANS_KEY, null)
            ?: return emptyList()

        // Si el contenido está dañado, se propaga el error.
        // Así evitamos sobrescribir planes existentes silenciosamente.
        val array = JSONArray(stored)

        val plans = List(array.length()) { index ->
            val item = array.getJSONObject(index)
            val destinationArray = item.getJSONArray("destinationIds")

            TravelPlan(
                id = item.getString("id"),
                title = item.getString("title"),
                days = item.getInt("days"),
                destinationIds = List(destinationArray.length()) {
                    destinationArray.getString(it)
                }
            )
        }

        require(plans.map { it.id }.distinct().size == plans.size) {
            "Hay identificadores de planes duplicados."
        }

        return plans
    }

    fun savePlan(plan: TravelPlan): List<TravelPlan> {
        val current = loadPlans()

        require(current.none { it.id == plan.id }) {
            "Este plan ya está guardado."
        }

        val updated = listOf(plan) + current
        val array = JSONArray()

        updated.forEach { savedPlan ->
            val destinations = JSONArray()

            savedPlan.destinationIds.forEach { id ->
                destinations.put(id)
            }

            array.put(
                JSONObject().apply {
                    put("id", savedPlan.id)
                    put("title", savedPlan.title)
                    put("days", savedPlan.days)
                    put("destinationIds", destinations)
                }
            )
        }

        // commit permite confirmar si la escritura fue exitosa.
        // El ViewModel ejecuta esta operación fuera del hilo de interfaz.
        val saved = preferences.edit()
            .putString(PLANS_KEY, array.toString())
            .commit()

        if (!saved) {
            throw IOException("No fue posible guardar el plan.")
        }

        return updated
    }
}