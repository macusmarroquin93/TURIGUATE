
package com.example.turiguate.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `actividades` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `itinerarioId` INTEGER NOT NULL,
                `day` INTEGER NOT NULL,
                `destination` TEXT NOT NULL,
                `description` TEXT NOT NULL,
                `restaurant` TEXT NOT NULL,
                `hotel` TEXT NOT NULL,
                `transport` TEXT NOT NULL,
                `price` TEXT NOT NULL,
                `imageUrl` TEXT NOT NULL,
                FOREIGN KEY(`itinerarioId`)
                    REFERENCES `itinerarios`(`id`)
                    ON UPDATE NO ACTION
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS
            `index_actividades_itinerarioId`
            ON `actividades` (`itinerarioId`)
            """.trimIndent()
        )
    }
}
