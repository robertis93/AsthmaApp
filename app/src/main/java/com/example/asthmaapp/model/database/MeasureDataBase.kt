package com.example.asthmaapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.DrugsAlarm
import com.example.asthmaapp.model.models.MedicalInfo


@Database(entities = [MeasureOfDay::class, TimeAndMeasure::class, MedicamentTime::class, Alarm::class, DrugsAlarm::class, MedicalInfo::class] , version = 1, exportSchema = false)
abstract class MeasureDataBase : RoomDatabase() {

    abstract fun MedAndMeasureDao() : DayDao
    abstract fun alarmDao(): AlarmDao
    abstract fun alarmDrugsDao(): AlarmDrugsDao
    abstract fun medicalInfoDao(): MedicalInfoDao


    //Singleton, MeasureDataBase
    companion object {
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database
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