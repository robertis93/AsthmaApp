package com.rightname.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rightname.asthmaapp.R
import com.rightname.asthmaapp.database.MeasureDataBase
import com.rightname.asthmaapp.model.Measure
import com.rightname.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.rightname.asthmaapp.viewmodel.repository.MedicamentAnalysesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InformationListViewModel(application: Application) : AndroidViewModel(application) {
    private val medicamentAnalysesRepository: MedicamentAnalysesRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        val medicamentDao = MeasureDataBase.getDataBase(application).medicamentInfoDao()
        medicamentAnalysesRepository =
            MedicamentAnalysesRepository(measurementsPerDayDao, medicamentDao)
    }

    val getAllMeasures: LiveData<List<Measure>> =
        medicamentAnalysesRepository.getAllMeasures

    val takeMedicamentTimeGroupByDate: MutableLiveData<List<MeasureWithTakeMedicamentTime>> =
        MutableLiveData()

    fun getAllMeasuresAndTakeMedicamentTime() {
        viewModelScope.launch(Dispatchers.IO) {
            takeMedicamentTimeGroupByDate.postValue(medicamentAnalysesRepository.getMeasureAndTakeMedicamentTimeGroupByDate())
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
            medicamentAnalysesRepository.deleteAllMeasure()
            medicamentAnalysesRepository.deleteAllTimeTakeMedicament()
            getAllMeasuresAndTakeMedicamentTime()
        }
    }
}

