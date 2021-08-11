package com.rightname.asthmaapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rightname.asthmaapp.model.Measure
import com.rightname.asthmaapp.model.MedicamentInfo
import com.rightname.asthmaapp.model.TakeMedicamentTime
import com.rightname.asthmaapp.model.TakeMedicamentTimeEntity

@Dao
interface MedicamentAnalysesDao {

    @Query("SELECT * FROM take_medicament_time_table ORDER BY dateTimeStamp")
    fun getAllTakeMedicamentTime(): LiveData<List<TakeMedicamentTime>>

    @Query("SELECT * FROM take_medicament_time_table ORDER BY dateTimeStamp")
    fun getListTakeMedicamentTime(): List<TakeMedicamentTime>

    @Query("SELECT * FROM measure_table ORDER BY dateTimestamp")
    fun getListMeasure(): List<Measure>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasure(measure: Measure)

    @Query("DELETE FROM take_medicament_time_table")
    suspend fun deleteAllTakeMedicamentTime()

    @Query("SELECT * FROM measure_table")
    fun readAllMeasure(): LiveData<List<Measure>>

    @Update
    suspend fun updateMeasure(measure: Measure)

    @Delete
    suspend fun deleteMeasure(measure: Measure)

    @Query("DELETE FROM measure_table")
    suspend fun deleteAllMeasure()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity)

    @Delete
    suspend fun deleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity)

    @Query("DELETE FROM take_medicament_time_table")
    suspend fun deleteTakeMedicamentTime()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMedicamentInfo(medicamentInfo: MedicamentInfo)

    @Delete
    suspend fun deleteMedicamentInfo(medicamentInfo: MedicamentInfo)

    @Query("DELETE FROM medicament_info_table")
    suspend fun deleteAllMedicamentInfo()

    @Query("SELECT * FROM medicament_info_table")
    suspend fun getListMedicamentInfo(): List<MedicamentInfo>
}

