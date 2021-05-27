package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.database.AlarmDao
import com.example.asthmaapp.model.database.AlarmDrugsDao
import com.example.asthmaapp.model.database.MeasureDao
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.DrugsAlarm


//Репозиторий управляет запросами и позволяет использовать несколько бэкендов.
// В наиболее распространенном примере
// Репозиторий реализует логику принятия решения о том,
// следует ли извлекать данные из сети или использовать результаты,
// кэшированные в локальной базе данных
class AlarmDrugsRepository(private val alarmDrugsDao: AlarmDrugsDao) {


    val readAllData: LiveData<List<DrugsAlarm>> = alarmDrugsDao.readAllData()

    suspend fun addAlarm(drugsAlarm : DrugsAlarm) {
        alarmDrugsDao.addAlarm(drugsAlarm)
    }

    suspend fun updateAlarm(drugsAlarm : DrugsAlarm) {
        alarmDrugsDao.updateAlarm(drugsAlarm)
    }

    suspend fun deleteAlarm(drugsAlarm : DrugsAlarm) {
        alarmDrugsDao.deleteAlarm(drugsAlarm)
    }

    suspend fun deleteAllAlarm() {
        alarmDrugsDao.deleteAllAlarms()
    }


}
