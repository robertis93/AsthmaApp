package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.model.DayWithMeasures
import com.example.asthmaapp.model.database.MeasureDataBase
import com.example.asthmaapp.viewmodel.repository.MeasureOfDayRepository
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.viewmodel.repository.MedicamentTimeRepository
import com.example.asthmaapp.viewmodel.repository.TimeAndMeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeasureOfDayViewModel(application: Application) : AndroidViewModel(application) {
    val measureDao = MeasureDataBase.getDataBase(application).dayDao()
    private val repository: MeasureOfDayRepository= MeasureOfDayRepository(measureDao)
    val readAllData: LiveData<List<MeasureOfDay>> = repository.readAllData
    val readDayWithMeasu: LiveData<List<DayWithMeasures>> = repository.dayWithMeasure

    //timeAndMeasure
    val timeAndMeasureDao = MeasureDataBase.getDataBase(application).timeAndMeasureDao()
    private val repositoryMeasure: TimeAndMeasureRepository= TimeAndMeasureRepository(timeAndMeasureDao)
    val readAllMeasures: LiveData<List<TimeAndMeasure>> = repositoryMeasure.readAllData
    //

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




    fun addTimeAndMeasure(timeAndMeasure: TimeAndMeasure) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryMeasure.addTimeMeasure(timeAndMeasure)
        }
    }

    fun updateTimeMeasure(timeAndMeasure: TimeAndMeasure){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryMeasure.updateTimeMeasure(timeAndMeasure)
        }
    }

    fun deleteTimeMeasure(timeAndMeasure: TimeAndMeasure){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryMeasure.deleteTimeMeasure(timeAndMeasure)
        }
    }

    fun deleteAllTimeMeasure(){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryMeasure.deleteAllTimeMeasures()
        }
    }
}



class TimeAndMeasureViewModel(application: Application) : AndroidViewModel(application) {

}

class MedicamentTimeViewModel(application: Application) : AndroidViewModel(application) {
    val medicamentTimeDao = MeasureDataBase.getDataBase(application).medicamentTimeDao()
    private val repository: MedicamentTimeRepository = MedicamentTimeRepository(medicamentTimeDao)
    val readAllData: LiveData<List<MedicamentTime>> = repository.readAllData

    fun addMedicalTime(medicamentTime: MedicamentTime) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTimeMeasure(medicamentTime)
        }
    }

    fun updateMedicalTime(medicamentTime: MedicamentTime){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTimeMeasure(medicamentTime)
        }
    }

    fun deleteMedicalTime(medicamentTime: MedicamentTime){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTimeMeasure(medicamentTime)
        }
    }

    fun deleteAllMedicalTime(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTimeMeasures()
        }
    }
}
