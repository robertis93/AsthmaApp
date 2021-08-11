package com.rightname.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rightname.asthmaapp.database.MeasureDataBase
import com.rightname.asthmaapp.model.MedicamentInfo
import com.rightname.asthmaapp.viewmodel.repository.MedicamentAnalysesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MedicamentViewModel(application: Application) : AndroidViewModel(application) {
    private val medicamentAnalysesRepository: MedicamentAnalysesRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        val medicamentDao = MeasureDataBase.getDataBase(application).medicamentInfoDao()
        medicamentAnalysesRepository =
            MedicamentAnalysesRepository(measurementsPerDayDao, medicamentDao)
    }

    suspend fun getInitMedicamentInfo(): MedicamentInfo? {
        val medicament = medicamentAnalysesRepository.getListMedicamentInfo()
        return if (medicament.isNotEmpty()) {
            medicament.last()
        } else
            null
    }

    fun addMedicamentInfo(medicamentName: String, medicamentDose: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val idMedicament: String = UUID.randomUUID().toString()
            val medicamentInfo =
                MedicamentInfo(idMedicament, medicamentName, medicamentDose.toInt())
            medicamentAnalysesRepository.addMedicamentInfo(medicamentInfo)
        }
    }
}


