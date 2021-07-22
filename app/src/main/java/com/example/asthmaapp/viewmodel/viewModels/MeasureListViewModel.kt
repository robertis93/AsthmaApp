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
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeasureListViewModel(application: Application) : AndroidViewModel(application) {
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

    fun getAllMeasuresAndTakeMedicamentTime() {
        viewModelScope.launch(Dispatchers.IO) {
            takeMedicamentTimeGroupByDate.postValue(measureRepository.getMeasureAndTakeMedicamentTimeGroupByDate())
        }
    }

    fun getColorForMeasure(measure: Measure): Int? {
        val maxMeasure = getAllMeasures.value?.maxByOrNull { it.measure }
        if (maxMeasure == null) return null

        val controlValue = maxMeasure.measure * 0.85
        val controlHighValue = maxMeasure.measure * 0.75

        val measureNow = measure.measure
        return when {
            measureNow < controlValue -> R.color.yelow
            measureNow < controlHighValue -> R.color.red
            else -> null
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

