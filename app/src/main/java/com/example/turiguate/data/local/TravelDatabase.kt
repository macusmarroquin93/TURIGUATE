package com.example.turiguate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TravelPlanEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TravelDatabase : RoomDatabase() {

    abstract fun planDao(): TravelPlanDao

    companion object {

        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getInstance(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "turiguate.db"
                )
                    .build()
                    .also { database ->
                        INSTANCE = database
                    }
            }
        }
    }
}