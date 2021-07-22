package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private val measureRepository: MeasureRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        measureRepository =
            MeasureRepository(measurementsPerDayDao)
    }

    fun addMeasure(dateTimeStamp : Long, measurePeakFlowMeter : Int) {
        val measure = Measure(
            0,
            dateTimeStamp,
            measurePeakFlowMeter
        )
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addMeasure(measure)
        }
    }

    fun addTakeMedicamentTime(medicamentDayTimeStamp: Long, dateTimeStamp: Long) {
        val medicamentTime =
            TakeMedicamentTimeEntity(
                0,
                medicamentDayTimeStamp,
                dateTimeStamp.toString()
            )
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addTakeMedicamentTime(medicamentTime)
        }
    }

    fun addTakeMedicament(dateTimeStamp: Long, nameMedicament: String,
                          doseMedicament: String) {
        val medicamentInfo =
            MedicamentInfo(
                dateTimeStamp.toString(),
                nameMedicament,
                doseMedicament.toInt()
            )
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.addMedicamentInfo(medicamentInfo)
        }
    }

    suspend fun getInitMedicamentInfo(): MedicamentInfo? {
        val medicament = measureRepository.getAllMedicamentInfoSync()
        return if (medicament.isNotEmpty()) {
            medicament.last()
        } else {
            null
        }
    }

}

