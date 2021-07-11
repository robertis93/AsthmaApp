package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.*
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias TakeMedicamentTimeGroupByDate = Map<String, List<TakeMedicamentTime>>

class MeasurementsPerDayViewModel(application: Application) : AndroidViewModel(application) {
    private val measureRepository: MeasureRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        measureRepository =
            MeasureRepository(measurementsPerDayDao)
    }

    val readAllMeasure: LiveData<List<Measure>> =
        measureRepository.readAllMeasure

//    val takeMedicamentTimeGroupByDate: LiveData<TakeMedicamentTimeGroupByDate> =
//        measurementsPerDayViewModelRepository.takeMedicamentTimeGroupByDate

    val takeMedicamentTimeGroupByDate: MutableLiveData<List<MeasureWithTakeMedicamentTime>> =
        MutableLiveData()
//    get() =
//        measurementsPerDayViewModelRepository.getMeasureAndTakeMedicamentTimeGroupByDate()

    val readAllData: LiveData<List<MedicamentInfo>> =
        measureRepository.readAllData

    // TODO: rename
    fun getAll() {
        viewModelScope.launch(Dispatchers.IO) {
            takeMedicamentTimeGroupByDate.postValue(measureRepository.getMeasureAndTakeMedicamentTimeGroupByDate())
        }
    }

    fun addTimeAndMeasure(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addTimeAndMeasure(measure)
        }
    }

    fun deleteAllMeasure() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllMeasure()
        }
    }

    fun updateTimeMeasure(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.updateTimeMeasure(measure)
        }
    }

    fun deleteTimeMeasure(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteTimeMeasure(measure)
        }
    }

    fun deleteAllTimeMeasure() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllTimeMeasure()
        }
    }

    fun addMedicalTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addMedicalTime(takeMedicamentTimeEntity)
        }
    }

    fun updateMedicalTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.updateMedicalTime(takeMedicamentTimeEntity)
        }
    }

    fun deleteMedicalTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteMedicalTime(takeMedicamentTimeEntity)
        }
    }

    fun deleteAllMedicalTime() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllMedicalTime()
        }
    }

    fun addMedicalInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addMedicalInfo(medicamentInfo)
        }
    }

    fun updateMedicalInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.updateMedicalInfo(medicamentInfo)
        }
    }

    fun deleteMedicalInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteMedicalInfo(medicamentInfo)
        }
    }

    fun deleteAllMedicalInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllMedicalInfo()
        }
    }
}
