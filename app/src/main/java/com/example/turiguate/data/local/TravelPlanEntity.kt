package com.example.turiguate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "travel_plans")
@TypeConverters(TravelPlanConverters::class)
data class TravelPlanEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    val days: Int,

    val destinationIds: List<String>,

    val createdAt: Long = System.currentTimeMillis()
)