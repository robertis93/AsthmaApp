package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeasurementsPerDayViewModel(application: Application) : AndroidViewModel(application) {

    val date = MutableLiveData<Long>()
    val measure = MutableLiveData<List<Measure>>()
    val timeTakeMedicament = MutableLiveData<List<TakeMedicamentTimeEntity>>()
    val medicamentInfo = MutableLiveData<MedicamentInfo>()

    private val measureRepository: MeasureRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        measureRepository =
            MeasureRepository(measurementsPerDayDao)
    }

    val getAllMeasures: LiveData<List<Measure>> =
        measureRepository.getAllMeasures

    val takeMedicamentTimeGroupByDate: MutableLiveData<List<MeasureWithTakeMedicamentTime>> =
        MutableLiveData()

    val getAllMedicamentInfo: LiveData<List<MedicamentInfo>> =
        measureRepository.getAllMedicamentInfo

    fun getAllMeasuresAndTakeMedicamentTime() {
        viewModelScope.launch(Dispatchers.IO) {
            takeMedicamentTimeGroupByDate.postValue(measureRepository.getMeasureAndTakeMedicamentTimeGroupByDate())
        }
    }

    fun addMeasure(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addMeasure(measure)
        }
    }

    fun deleteAllTimeTakeMedicament() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllTimeTakeMedicament()
        }
    }

    fun updateMeasure(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.updateMeasure(measure)
        }
    }

    fun deleteMeasure(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteMeasure(measure)
        }
    }

    fun deleteAllMeasure() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllMeasure()
        }
    }

    fun addTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addTakeMedicamentTime(takeMedicamentTimeEntity)
        }
    }

    fun updateTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.updateTakeMedicamentTime(takeMedicamentTimeEntity)
        }
    }

    fun deleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
        }
    }

    fun deleteAllTakeMedicamentTime() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllTakeMedicamentTime()
        }
    }

    fun addMedicamentInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addMedicamentInfo(medicamentInfo)
        }
    }

    fun updateMedicamentInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.updateMedicamentInfo(medicamentInfo)
        }
    }

    fun deleteMedicamentInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteMedicamentInfo(medicamentInfo)
        }
    }

    fun deleteAllMedicamentInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllMedicamentInfo()
        }
    }

    fun deleteAllMeasuresWithMedicaments() {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteAllMeasure()
            measureRepository.deleteAllTimeTakeMedicament()
            getAllMeasuresAndTakeMedicamentTime()
        }
    }
}
