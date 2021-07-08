package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData

import com.example.asthmaapp.database.MedicamentInfoDao

import com.example.asthmaapp.model.MedicamentInfo

class MedicalInfoRepository(private val medicamentInfoDao: MedicamentInfoDao) {

    val readAllData: LiveData<List<MedicamentInfo>> = medicamentInfoDao.readAllData()

    suspend fun addMedicalInfo(medicamentInfo: MedicamentInfo) {
        medicamentInfoDao.addMedicalInfo(medicamentInfo)
    }

    suspend fun updateMedicalInfo(medicamentInfo: MedicamentInfo) {
        medicamentInfoDao.updateMedicalInfo(medicamentInfo)
    }

    suspend fun deleteMedicalInfo(medicamentInfo: MedicamentInfo) {
        medicamentInfoDao.deleteMedicalInfo(medicamentInfo)
    }

    suspend fun deleteAllMedicalInfo() {
        medicamentInfoDao.deleteAllMedicalInfo()
    }
}
