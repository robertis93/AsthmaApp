package com.example.asthmaapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTime
import com.example.asthmaapp.model.TakeMedicamentTimeEntity

@Dao
interface MeasurementsPerDayDao {

    @Query("SELECT * FROM time_take_medicament_table ORDER BY id")
    fun getAllTakeMedicamentTime(): LiveData<List<TakeMedicamentTime>>

    @Query("SELECT * FROM time_take_medicament_table ORDER BY dateTimestamp")
    fun getAllTakeMedicamentTimeList() : List<TakeMedicamentTime>

    @Query("SELECT * FROM measure_table ORDER BY dateTimestamp")
    fun getAllMeasureList() : List<Measure>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasure(measure: Measure)

    @Query("DELETE FROM time_take_medicament_table")
    suspend fun deleteAllTimeTakeMedicament()

    @Query("SELECT * FROM measure_table")
    fun readAllMeasure(): LiveData<List<Measure>>

    @Query("SELECT * FROM measure_table")
    fun getAllMeasure(): List<Measure>

    @Update
    suspend fun updateMeasure(measure: Measure)

    @Delete
    suspend fun deleteMeasure(measure: Measure)

    @Query("DELETE FROM measure_table")
    suspend fun deleteAllMeasure()

    @Query("SELECT * FROM time_take_medicament_table")
    fun readAllTakeMedicamentTime(): LiveData<List<TakeMedicamentTimeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity)

    @Update
    suspend fun updateTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity)

    @Delete
    suspend fun deleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity)

    @Query("DELETE FROM time_take_medicament_table")
    suspend fun deleteTakeMedicamentTime()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMedicamentInfo(medicamentInfo: MedicamentInfo)

    @Update
    suspend fun updateMedicamentInfo(medicamentInfo: MedicamentInfo)

    @Delete
    suspend fun deleteMedicamentInfo(medicamentInfo: MedicamentInfo)

    @Query("DELETE FROM medicament_info_table")
    suspend fun deleteAllMedicamentInfo()

    @Query("SELECT * FROM medicament_info_table")
    fun readAllMedicamentInfo(): LiveData<List<MedicamentInfo>>
}

