package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.database.AlarmDao
import com.example.asthmaapp.model.database.MeasureDao
import com.example.asthmaapp.model.models.Alarm


//Репозиторий управляет запросами и позволяет использовать несколько бэкендов.
// В наиболее распространенном примере
// Репозиторий реализует логику принятия решения о том,
// следует ли извлекать данные из сети или использовать результаты,
// кэшированные в локальной базе данных
class AlarmRepository(private val alarmDao: AlarmDao) {


    val readAllData: LiveData<List<Alarm>> = alarmDao.readAllData()

    suspend fun addAlarm(alarm: Alarm) {
        alarmDao.addAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    suspend fun deleteAllAlarm() {
        alarmDao.deleteAllAlarms()
    }


}
