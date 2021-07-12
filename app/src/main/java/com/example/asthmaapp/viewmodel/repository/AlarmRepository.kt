package com.example.asthmaapp.viewmodel.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.asthmaapp.database.AlarmDao
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.view.fragments.TypeAlarm

class AlarmRepository(private val alarmDao: AlarmDao) {

    fun getAllAlarms(context: Context, typeAlarm: TypeAlarm): LiveData<List<Alarm>>? {
        return alarmDao.getAllAlarm(typeAlarm)
    }

    suspend fun addAlarm(alarm: Alarm) {
        alarmDao.addAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    suspend fun deleteAllAlarms() {
        alarmDao.deleteAllAlarms()
    }
}
