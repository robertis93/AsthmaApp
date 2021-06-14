package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.asthmaapp.model.database.MeasureDataBase
import com.example.asthmaapp.model.models.DrugsAlarm
import com.example.asthmaapp.viewmodel.repository.AlarmDrugsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmDrugsViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<DrugsAlarm>>
    private val repository: AlarmDrugsRepository

    init {
        val alarmDrugsDao = MeasureDataBase.getDataBase(application).alarmDrugsDao()
        repository = AlarmDrugsRepository(alarmDrugsDao)
        readAllData = repository.readAllData
    }

    fun addAlarm(drugsAlarm : DrugsAlarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(drugsAlarm)
        }
    }

    fun updateAlarm(drugsAlarm : DrugsAlarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlarm(drugsAlarm)
        }
    }

    fun deleteAlarm(drugsAlarm : DrugsAlarm){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(drugsAlarm)
        }
    }

    fun deleteAllAlarms(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAlarm()
        }
    }
}