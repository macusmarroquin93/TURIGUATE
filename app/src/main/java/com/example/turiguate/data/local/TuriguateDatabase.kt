
package com.example.turiguate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ItinerarioEntity::class,
        ActividadEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TuriguateDatabase : RoomDatabase() {

    abstract fun itinerarioDao(): ItinerarioDao
    abstract fun actividadDao(): ActividadDao
}
