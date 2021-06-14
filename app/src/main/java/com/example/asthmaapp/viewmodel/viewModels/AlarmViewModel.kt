package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.asthmaapp.model.database.MeasureDataBase
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.viewmodel.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Alarm>>
    private val repository: AlarmRepository

    init {
        val alarmDao = MeasureDataBase.getDataBase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        readAllData = repository.readAllData
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun updateAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }

    fun deleteAllAlarms(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAlarm()
        }
    }
}