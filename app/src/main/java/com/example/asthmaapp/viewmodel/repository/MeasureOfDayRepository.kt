package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.asthmaapp.model.DayWithMeasures
import com.example.asthmaapp.model.database.MeasureDao
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.database.MedicalTimeDao
import com.example.asthmaapp.model.database.TimeAndMeasureDao

class MeasureOfDayRepository(private val measureDao: MeasureDao) {

    val readAllData: LiveData<List<MeasureOfDay>> = measureDao.readAllData()
    val dayWithMeasure= measureDao.getDaysWithMeasures()

    suspend fun addMeasure(measureOfDay: MeasureOfDay){
        measureDao.addMeasureOfDay(measureOfDay)
    }

    suspend fun updateMeasure(measureOfDay: MeasureOfDay){
        measureDao.updateMeasure(measureOfDay)
    }

    suspend fun deleteMeasure(measureOfDay: MeasureOfDay){
        measureDao.deleteMeasure(measureOfDay)
    }

    suspend fun deleteAllMeasures(){
        measureDao.deleteAllMeasure()
    }


}

class TimeAndMeasureRepository(private val timeAndMeasureDao: TimeAndMeasureDao) {

    val readAllData: LiveData<List<TimeAndMeasure>> = timeAndMeasureDao.readAllData()

    suspend fun addTimeMeasure(timeAndMeasure: TimeAndMeasure){
        timeAndMeasureDao.addTimeAndMeasure(timeAndMeasure)
    }

    suspend fun updateTimeMeasure(timeAndMeasure: TimeAndMeasure){
        timeAndMeasureDao.updateTimeAndMeasure(timeAndMeasure)
    }

    suspend fun deleteTimeMeasure(timeAndMeasure: TimeAndMeasure){
        timeAndMeasureDao.deleteTimeAndMeasure(timeAndMeasure)
    }

    suspend fun deleteAllTimeMeasures(){
        timeAndMeasureDao.deleteAllTimeAndMeasure()
    }

}

class MedicamentTimeRepository(private val medicalTimeDao: MedicalTimeDao) {

    val readAllData: LiveData<List<MedicamentTime>> =  medicalTimeDao.readAllData()

    suspend fun addTimeMeasure(medicamentTime: MedicamentTime){
        medicalTimeDao.addMedicalTime(medicamentTime)
    }

    suspend fun updateTimeMeasure(medicamentTime: MedicamentTime){
        medicalTimeDao.updateMedicalTime(medicamentTime)
    }

    suspend fun deleteTimeMeasure(medicamentTime: MedicamentTime){
        medicalTimeDao.deleteMedicalTime(medicamentTime)
    }

    suspend fun deleteAllTimeMeasures(){
        medicalTimeDao.deleteAllMedicalTime()
    }

}