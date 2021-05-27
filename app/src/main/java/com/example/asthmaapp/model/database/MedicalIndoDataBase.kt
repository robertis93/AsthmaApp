package com.example.asthmaapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.MedicalInfo

@Database(entities = [MedicalInfo::class], version = 1, exportSchema = false)
abstract class MedicalIndoDataBase : RoomDatabase() {

    abstract fun medicalInfoDao(): MedicalInfoDao

    //Singlton, AlarmDataBase
    companion object {
        //Singleton предотвращает одновременное открытие нескольких экземпляров базы данных
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database

        @Volatile
        private var INSTANCE: MedicalIndoDataBase? = null

        fun getDataBase(context: Context): MedicalIndoDataBase {
            // если ЭКЗЕМПЛЯР не равен нулю, то верните его,
// если это так, то создайте базу данных

            val tempInstance = MedicalIndoDataBase.INSTANCE
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
                    MedicalIndoDataBase::class.java,
                    "medicalinfo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}