package com.example.asthmaapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asthmaapp.model.MeasureOfDay

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