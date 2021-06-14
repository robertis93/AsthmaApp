package com.example.asthmaapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.*


@Dao
interface DayDao {

    @Query("SELECT * FROM medicament_day_table")
    fun getMedicamentAndMeasures(): LiveData<List<MedWithMeasuresAndMedicamentTime>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicament(measureOfDay: MeasureOfDay)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeAndMeasure(timeAndMeasure: TimeAndMeasure)

    //MeasureOfDay (Medicament class) update, delete
    @Query("SELECT * FROM medicament_day_table")
    fun readAllMedicament(): LiveData<List<MeasureOfDay>>

    @Update
    suspend fun updateMedicament(measureOfDay: MeasureOfDay)

    @Delete
    suspend fun deleteMedicament(measureOfDay: MeasureOfDay)

    @Query("DELETE FROM medicament_day_table")
    suspend fun deleteAllMeasure()

    //TimeAndMeasure read, update, delete, deleteAll
    @Query("SELECT * FROM time_measure_table")
    fun readAllTimeAndMeasure(): LiveData<List<TimeAndMeasure>>

    @Update
    suspend fun updateTimeAndMeasure(timeAndMeasure: TimeAndMeasure)

    @Delete
    suspend fun deleteTimeAndMeasure(timeAndMeasure: TimeAndMeasure)

    @Query("DELETE FROM time_measure_table")
    suspend fun deleteAllTimeAndMeasure()

    //MedicalTime read, add, update, delete
    @Query("SELECT * FROM medicament_time_table ")
    fun readAllMedicalTime(): LiveData<List<MedicamentTime>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMedicalTime(medicamentTime: MedicamentTime)

    @Update
    suspend fun updateMedicalTime(medicamentTime: MedicamentTime)

    @Delete
    suspend fun deleteMedicalTime(medicamentTime: MedicamentTime)

    @Query("DELETE FROM medicament_time_table")
    suspend fun deleteAllMedicalTime()

}

