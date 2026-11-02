package com.example.turiguate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelPlanDao {

    @Query(
        """
        SELECT * FROM travel_plans
        ORDER BY createdAt DESC, id ASC
        """
    )
    fun observePlans(): Flow<List<TravelPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlan(plan: TravelPlanEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun importPlans(plans: List<TravelPlanEntity>)
}