package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.viewmodel.repository.MedicalInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicamentViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<MedicamentInfo>>
    private val repository: MedicalInfoRepository

    init {
        val medicalIndoDao = MeasureDataBase.getDataBase(application).medicalInfoDao()
        repository = MedicalInfoRepository(medicalIndoDao)
        readAllData = repository.readAllData
    }

    fun addMedicalInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMedicalInfo(medicamentInfo)
        }
    }

    fun updateMedicalInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMedicalInfo(medicamentInfo)
        }
    }

    fun deleteMedicalInfo(medicamentInfo: MedicamentInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMedicalInfo(medicamentInfo)
        }
    }

    fun deleteAllMedicalInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMedicalInfo()
        }
    }
}