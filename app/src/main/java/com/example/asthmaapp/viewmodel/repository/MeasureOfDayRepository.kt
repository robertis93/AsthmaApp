package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.asthmaapp.model.database.MeasureDao
import com.example.asthmaapp.model.MeasureOfDay

class MeasureOfDayRepository(private val measureDao: MeasureDao) {

    val readAllData: LiveData<List<MeasureOfDay>> = measureDao.readAllData()

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