
package com.example.turiguate

import android.app.Application
import androidx.room.Room
import com.example.turiguate.data.local.TuriguateDatabase
import com.example.turiguate.data.repository.ItinerarioRepository
import com.example.turiguate.data.local.MIGRATION_1_2

class TuriguateApplication : Application() {

    val database: TuriguateDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            TuriguateDatabase::class.java,
            "turiguate_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    val itinerarioRepository: ItinerarioRepository by lazy {
        ItinerarioRepository(
            itinerarioDao = database.itinerarioDao(),
            database = database
        )
    }
}
