package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.view.fragments.TypeAlarm
import com.example.asthmaapp.viewmodel.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    val alarmMeasureListLiveData = MutableLiveData<List<Alarm>>()
    val alarmMedicamentListLiveData = MutableLiveData<List<Alarm>>()

    private val repository: AlarmRepository

    init {
        val alarmDao = MeasureDataBase.getDataBase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        getListAlarm()
    }

    private fun getListAlarm() {
        viewModelScope.launch {
            alarmMeasureListLiveData.value = repository.getAllAlarms(TypeAlarm.MEASURE)
            alarmMedicamentListLiveData.value = repository.getAllAlarms(TypeAlarm.MEDICAMENT)
        }
    }

    fun addAlarm(id: String, hour: Int, minute: Int, alarmType: TypeAlarm) {
        val alarm = Alarm(id, hour, minute, alarmType)
        if (alarmType == TypeAlarm.MEASURE) {
            alarmMeasureListLiveData.value?.let { listMeasure ->
                val measureMutableList = listMeasure.toMutableList()
                measureMutableList.add(alarm)
                alarmMeasureListLiveData.value = measureMutableList
            }
        } else {
            alarmMedicamentListLiveData.value?.let { listMeasure ->
                val measureMutableList = listMeasure.toMutableList()
                measureMutableList.add(alarm)
                alarmMedicamentListLiveData.value = measureMutableList
            }
        }
        viewModelScope.launch {
            repository.addAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        if (alarm.typeAlarm == TypeAlarm.MEASURE) {
            alarmMeasureListLiveData.value?.let { listAlarm ->
                val measureMutableList = listAlarm.toMutableList()
                measureMutableList.remove(alarm)
                alarmMeasureListLiveData.value = measureMutableList
            }
        } else {
            alarmMedicamentListLiveData.value?.let { listAlarm ->
                val measureMutableList = listAlarm.toMutableList()
                measureMutableList.remove(alarm)
                alarmMedicamentListLiveData.value = measureMutableList
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }
}