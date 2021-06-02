 package com.example.asthmaapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.MeasureOfDay

@Dao
interface MeasureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMeasureOfDay(measureOfDay: MeasureOfDay)

    @Update
    suspend fun updateMeasure(measureOfDay: MeasureOfDay)

    @Delete
    suspend fun deleteMeasure(measureOfDay: MeasureOfDay)

    @Query("DELETE FROM measure_of_day_table")
    suspend fun deleteAllMeasure()


    @Query("Select * From measure_of_day_table")
    fun readAllData(): LiveData<List<MeasureOfDay>>
}