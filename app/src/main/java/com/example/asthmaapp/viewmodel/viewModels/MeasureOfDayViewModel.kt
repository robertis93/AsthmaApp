package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.model.database.MeasureDataBase
import com.example.asthmaapp.viewmodel.repository.MeasureOfDayRepository
import com.example.asthmaapp.model.MeasureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeasureOfDayViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<MeasureOfDay>>
    private val repository: MeasureOfDayRepository

    init {
        val measureDao = MeasureDataBase.getDataBase(application).measureDao()
        repository = MeasureOfDayRepository(measureDao)
        readAllData = repository.readAllData
    }

    fun addMeasure(measureOfDay: MeasureOfDay) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMeasure(measureOfDay)
        }
    }

    fun updateMeasure(measureOfDay: MeasureOfDay){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMeasure(measureOfDay)
        }
    }

    fun deleteMeasure(measureOfDay: MeasureOfDay){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMeasure(measureOfDay)
        }
    }

    fun deleteAllMeasure(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMeasures()
        }
    }
}