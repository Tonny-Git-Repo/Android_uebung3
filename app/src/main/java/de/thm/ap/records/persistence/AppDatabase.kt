package de.thm.ap.records.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.thm.ap.records.model.Record

@Database(entities = [Record::class], version = 1)

abstract class AppDatabase: RoomDatabase() {

    abstract fun recordDao(): RecordDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDb(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { INSTANCE = it }
            }
        }
    }

}