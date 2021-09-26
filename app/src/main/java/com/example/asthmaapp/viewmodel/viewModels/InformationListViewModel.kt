package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.R
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.example.asthmaapp.viewmodel.repository.InformationPerDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InformationListViewModel(application: Application) : AndroidViewModel(application) {
    private val informationPerDayRepository: InformationPerDayRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        informationPerDayRepository =
            InformationPerDayRepository(measurementsPerDayDao)
    }

    val getAllMeasures: LiveData<List<Measure>> =
        informationPerDayRepository.getAllMeasures

    val takeMedicamentTimeGroupByDate: MutableLiveData<List<MeasureWithTakeMedicamentTime>> =
        MutableLiveData()

    fun getAllMeasuresAndTakeMedicamentTime() {
        viewModelScope.launch(Dispatchers.IO) {
            takeMedicamentTimeGroupByDate.postValue(informationPerDayRepository.getMeasureAndTakeMedicamentTimeGroupByDate())
        }
    }

    fun getColorForMeasure(measure: Measure): Int? {
        val maxMeasure = getAllMeasures.value?.maxByOrNull { it.value } ?: return null

        val controlValue = maxMeasure.value * 0.85
        val controlHighValue = maxMeasure.value * 0.75

        val measureNow = measure.value
        return when {
            measureNow < controlHighValue -> R.color.red
            measureNow < controlValue -> R.color.yelow
            else -> null
        }
    }

    fun deleteAllMeasuresWithMedicaments() {
        viewModelScope.launch(Dispatchers.IO) {
            informationPerDayRepository.deleteAllMeasure()
            informationPerDayRepository.deleteAllTimeTakeMedicament()
            getAllMeasuresAndTakeMedicamentTime()
        }
    }
}

