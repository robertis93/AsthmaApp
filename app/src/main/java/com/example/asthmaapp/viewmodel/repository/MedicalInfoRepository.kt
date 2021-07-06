package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData

import com.example.asthmaapp.model.database.MedicalInfoDao

import com.example.asthmaapp.model.models.MedicamentlInfo


//Репозиторий управляет запросами и позволяет использовать несколько бэкендов.
// В наиболее распространенном примере
// Репозиторий реализует логику принятия решения о том,
// следует ли извлекать данные из сети или использовать результаты,
// кэшированные в локальной базе данных
class MedicalInfoRepository(private val medicalInfoDao: MedicalInfoDao) {


    val readAllData: LiveData<List<MedicamentlInfo>> = medicalInfoDao.readAllData()

    suspend fun addMedicalInfo(medicamentlInfo: MedicamentlInfo) {
        medicalInfoDao.addMedicalInfo(medicamentlInfo)
    }

    suspend fun updateMedicalInfo(medicamentlInfo: MedicamentlInfo) {
        medicalInfoDao.updateMedicalInfo(medicamentlInfo)
    }

    suspend fun deleteMedicalInfo(medicamentlInfo: MedicamentlInfo) {
        medicalInfoDao.deleteMedicalInfo(medicamentlInfo)
    }

    suspend fun deleteAllMedicalInfo() {
        medicalInfoDao.deleteAllMedicalInfo()
    }


}
