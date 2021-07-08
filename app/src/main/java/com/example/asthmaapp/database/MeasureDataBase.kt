package com.example.asthmaapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asthmaapp.model.*

@Database(
    entities = [MeasureOfDay::class, TimeAndMeasure::class, MedicamentTime::class, Alarm::class, MedicamentInfo::class],
    version = 1,
    exportSchema = false
)
abstract class MeasureDataBase : RoomDatabase() {

    // TODO: rename to dayMeasureDao()
    abstract fun medAndMeasureDao(): MeasurementsPerDayDao
    abstract fun alarmDao(): AlarmDao
    // TODO: rename to medicamentInfoDao()
    abstract fun medicalInfoDao(): MedicamentInfoDao

    companion object {
        @Volatile
        private var INSTANCE: MeasureDataBase? = null

        fun getDataBase(context: Context): MeasureDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MeasureDataBase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}