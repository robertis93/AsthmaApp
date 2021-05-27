package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.model.database.MedicalIndoDataBase
import com.example.asthmaapp.model.models.MedicalInfo
import com.example.asthmaapp.viewmodel.repository.MedicalInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicalViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<MedicalInfo>>
    private val repository: MedicalInfoRepository

    init {
        val medicalIndoDao = MedicalIndoDataBase.getDataBase(application).medicalInfoDao()
        repository = MedicalInfoRepository(medicalIndoDao)
        readAllData = repository.readAllData
    }

    fun addMedicalInfo(medicalInfo: MedicalInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMedicalInfo(medicalInfo)
        }
    }

    fun updateMedicalInfo(medicalInfo: MedicalInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMedicalInfo(medicalInfo)
        }
    }

    fun deleteMedicalInfo(medicalInfo: MedicalInfo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMedicalInfo(medicalInfo)
        }
    }

    fun deleteAllMedicalInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMedicalInfo()
        }
    }
}