package com.rightname.asthmaapp.viewmodel.repository

import com.rightname.asthmaapp.database.AlarmDao
import com.rightname.asthmaapp.model.Alarm
import com.rightname.asthmaapp.view.fragments.TypeAlarm

class AlarmRepository(private val alarmDao: AlarmDao) {

    suspend fun getAllAlarms(typeAlarm: TypeAlarm): List<Alarm>? {
        return alarmDao.getListAlarm(typeAlarm)
    }

    suspend fun addAlarm(alarm: Alarm) {
        alarmDao.addAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }
}
