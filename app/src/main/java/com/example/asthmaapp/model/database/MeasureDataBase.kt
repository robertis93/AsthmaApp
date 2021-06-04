package com.example.asthmaapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure

@Database(entities = [MeasureOfDay::class], version = 1, exportSchema = false)
abstract class MeasureDataBase : RoomDatabase() {

    abstract fun measureDao(): MeasureDao

    //Singlton, MeasureDataBase
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
                    "measure_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

@Database(entities = [TimeAndMeasure::class], version = 1, exportSchema = false)
abstract class TimeAndMeasureDataBase : RoomDatabase() {

    abstract fun timeAndMeasureDao(): TimeAndMeasureDao

    //Singlton, Time&MeasureDataBase
    companion object {
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database
        @Volatile
        private var INSTANCE: TimeAndMeasureDataBase? = null

        fun getDataBase(context: Context): TimeAndMeasureDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimeAndMeasureDataBase::class.java,
                    "time_measure_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

@Database(entities = [MedicamentTime::class], version = 1, exportSchema = false)
abstract class MedicamentTimeDataBase : RoomDatabase() {

    abstract fun medicamentTimeDao(): MedicalTimeDao

    //Singlton, Time&MeasureDataBase
    companion object {
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database
        @Volatile
        private var INSTANCE: MedicamentTimeDataBase? = null

        fun getDataBase(context: Context): MedicamentTimeDataBase  {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicamentTimeDataBase::class.java,
                    "medicament_time_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}