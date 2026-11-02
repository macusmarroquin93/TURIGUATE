package com.example.turiguate.data.local

import androidx.room.TypeConverter
import org.json.JSONArray

class TravelPlanConverters {

    @TypeConverter
    fun fromDestinationIds(ids: List<String>): String {
        val array = JSONArray()

        ids.forEach { id ->
            array.put(id)
        }

        return array.toString()
    }

    @TypeConverter
    fun toDestinationIds(value: String): List<String> {
        val array = JSONArray(value)

        return List(array.length()) { index ->
            array.getString(index)
        }
    }
}
