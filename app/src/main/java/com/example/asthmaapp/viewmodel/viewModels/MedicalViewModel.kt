package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.model.database.MeasureDataBase
import com.example.asthmaapp.model.models.MedicamentlInfo
import com.example.asthmaapp.viewmodel.repository.MedicalInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicalViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<MedicamentlInfo>>
    private val repository: MedicalInfoRepository

    init {
        val medicalIndoDao = MeasureDataBase.getDataBase(application).medicalInfoDao()
        repository = MedicalInfoRepository(medicalIndoDao)
        readAllData = repository.readAllData
    }

    fun addMedicalInfo(medicamentlInfo: MedicamentlInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMedicalInfo(medicamentlInfo)
        }
    }

    fun updateMedicalInfo(medicamentlInfo: MedicamentlInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMedicalInfo(medicamentlInfo)
        }
    }

    fun deleteMedicalInfo(medicamentlInfo: MedicamentlInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMedicalInfo(medicamentlInfo)
        }
    }

    fun deleteAllMedicalInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMedicalInfo()
        }
    }
}