package com.example.asthmaapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.DrugsAlarm

@Database(entities = [DrugsAlarm::class], version = 1, exportSchema = false)
abstract class AlarmDrugsDataBase : RoomDatabase() {

    abstract fun alarmDrugsDao(): AlarmDrugsDao

    //Singlton, AlarmDrugsDataBase
    companion object {
        //Singleton предотвращает одновременное открытие нескольких экземпляров базы данных
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database

        @Volatile
        private var INSTANCE: AlarmDrugsDataBase? = null

        fun getDataBase(context: Context): AlarmDrugsDataBase {
            // если ЭКЗЕМПЛЯР не равен нулю, то верните его,
// если это так, то создайте базу данных

            val tempInstance = AlarmDrugsDataBase.INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {

                // return INSTANCE ?: synchronized(this) {

                //По умолчанию, чтобы избежать низкой производительности пользовательского интерфейса,
                // Room не позволяет выполнять запросы в основном потоке.
                // Когда запросы комнат возвращают поток, запросы автоматически выполняются асинхронно в фоновом потоке.

                //Создание объекта базы данных в классе UsersDatabase
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDrugsDataBase::class.java,
                    "alarm_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}