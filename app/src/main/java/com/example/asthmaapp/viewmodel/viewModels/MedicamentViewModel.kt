package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MedicamentViewModel(application: Application) : AndroidViewModel(application) {
    val medicamentInfoLiveData = MutableLiveData<MedicamentInfo>()
    private val measureRepository: MeasureRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        measureRepository =
            MeasureRepository(measurementsPerDayDao)
        getLastMedicament()
    }

    private fun getLastMedicament() {
        viewModelScope.launch {
            val medicament = measureRepository.getAllMedicamentInfoSync()
            if (medicament.isNotEmpty()) {
                val lastIndex = medicament.last()
                val idMedicament: String = UUID.randomUUID().toString()
                val medicamentNew =
                    MedicamentInfo(idMedicament, lastIndex.name, lastIndex.dose)
                medicamentInfoLiveData.value = medicamentNew
            }
        }
    }

    fun changeMedicamentName(nameMedicament: String) {
        medicamentInfoLiveData.value?.name = nameMedicament
    }

    fun changeMedicamentDose(doseMedicament: String) {
        if (doseMedicament == "") {
            medicamentInfoLiveData.value?.dose = 0
        } else
            medicamentInfoLiveData.value?.dose = doseMedicament.toInt()
    }

    fun addMedicamentInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val medicament: MedicamentInfo
            medicament = medicamentInfoLiveData.value!!
            measureRepository.addMedicamentInfo(medicament)
        }
    }
}


