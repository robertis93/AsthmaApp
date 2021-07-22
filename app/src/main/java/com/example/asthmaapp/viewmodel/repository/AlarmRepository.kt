package com.example.asthmaapp.viewmodel.repository

import com.example.asthmaapp.database.AlarmDao
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.view.fragments.TypeAlarm

class AlarmRepository(private val alarmDao: AlarmDao) {

    suspend fun getAllAlarms(typeAlarm: TypeAlarm): List<Alarm>? {
        return alarmDao.getAllAlarm(typeAlarm)
    }

    suspend fun addAlarm(alarm: Alarm) {
        alarmDao.addAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }
}
