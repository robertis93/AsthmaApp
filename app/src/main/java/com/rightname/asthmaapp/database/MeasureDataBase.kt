package com.rightname.asthmaapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rightname.asthmaapp.model.Alarm
import com.rightname.asthmaapp.model.Measure
import com.rightname.asthmaapp.model.MedicamentInfo
import com.rightname.asthmaapp.model.TakeMedicamentTimeEntity

@Database(
    entities = [Measure::class, TakeMedicamentTimeEntity::class, Alarm::class, MedicamentInfo::class],
    version = 1,
    exportSchema = false
)
abstract class MeasureDataBase : RoomDatabase() {

    abstract fun measurementsPerDayDao(): MedicamentAnalysesDao
    abstract fun alarmDao(): AlarmDao
    abstract fun medicamentInfoDao(): MedicamentInfoDao

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