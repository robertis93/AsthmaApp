package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.view.fragments.TypeAlarm
import com.example.asthmaapp.viewmodel.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    var readAllAlarms: LiveData<List<Alarm>>? = null
    private val repository: AlarmRepository

    fun getListAlarm(context: Context, typeAlarm: TypeAlarm) : LiveData<List<Alarm>>? {
        readAllAlarms = repository.getAlarms(context, typeAlarm)
        return readAllAlarms
    }

    init {
        val alarmDao = MeasureDataBase.getDataBase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
       // readAllData = repository.readAllData
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }

    fun deleteAllAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAlarm()
        }
    }
}