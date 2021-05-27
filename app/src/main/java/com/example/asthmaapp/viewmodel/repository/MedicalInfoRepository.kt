package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.database.AlarmDao
import com.example.asthmaapp.model.database.AlarmDrugsDao
import com.example.asthmaapp.model.database.MeasureDao
import com.example.asthmaapp.model.database.MedicalInfoDao
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.DrugsAlarm
import com.example.asthmaapp.model.models.MedicalInfo


//Репозиторий управляет запросами и позволяет использовать несколько бэкендов.
// В наиболее распространенном примере
// Репозиторий реализует логику принятия решения о том,
// следует ли извлекать данные из сети или использовать результаты,
// кэшированные в локальной базе данных
class MedicalInfoRepository(private val medicalInfoDao: MedicalInfoDao) {


    val readAllData: LiveData<List<MedicalInfo>> = medicalInfoDao.readAllData()

    suspend fun addMedicalInfo(medicalInfo: MedicalInfo) {
        medicalInfoDao.addMedicalInfo(medicalInfo)
    }

    suspend fun updateMedicalInfo(medicalInfo: MedicalInfo) {
        medicalInfoDao.updateMedicalInfo(medicalInfo)
    }

    suspend fun deleteMedicalInfo(medicalInfo: MedicalInfo) {
        medicalInfoDao.deleteMedicalInfo(medicalInfo)
    }

    suspend fun deleteAllMedicalInfo() {
        medicalInfoDao.deleteAllMedicalInfo()
    }


}
