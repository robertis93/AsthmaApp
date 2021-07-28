package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.viewmodel.repository.InformationPerDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private val informationPerDayRepository: InformationPerDayRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        informationPerDayRepository =
            InformationPerDayRepository(measurementsPerDayDao)
    }

    fun addMeasure(dateTimeStamp: Long, measurePeakFlowMeter: Int) {
        val measure = Measure(
            0,
            dateTimeStamp,
            measurePeakFlowMeter
        )
        viewModelScope.launch(Dispatchers.IO) {
            informationPerDayRepository.addMeasure(measure)
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
            informationPerDayRepository.addTakeMedicamentTime(medicamentTime)
        }
    }

    fun addTakeMedicament(
        dateTimeStamp: Long, nameMedicament: String,
        doseMedicament: String
    ) {
        val medicamentInfo =
            MedicamentInfo(
                dateTimeStamp.toString(),
                nameMedicament,
                doseMedicament.toInt()
            )
        viewModelScope.launch(Dispatchers.IO) {
            informationPerDayRepository.addMedicamentInfo(medicamentInfo)
        }
    }

    suspend fun getInitMedicamentInfo(): MedicamentInfo? {
        val medicament = informationPerDayRepository.getListMedicamentInfo()
        return if (medicament.isNotEmpty()) {
            medicament.last()
        } else {
            null
        }
    }
}

