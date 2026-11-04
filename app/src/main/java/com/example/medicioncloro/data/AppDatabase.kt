package com.example.medicioncloro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Medicion::class, Alerta::class, Desviacion::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicionDao(): MedicionDao
    abstract fun alertaDao(): AlertaDao
    abstract fun desviacionDao(): DesviacionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mediciones_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}