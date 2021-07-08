package com.example.asthmaapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.MedicamentInfo

@Dao
interface MedicamentInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMedicalInfo(medicamentInfo: MedicamentInfo)

    @Update
    suspend fun updateMedicalInfo(medicamentInfo: MedicamentInfo)

    @Delete
    suspend fun deleteMedicalInfo(medicamentInfo: MedicamentInfo)

    @Query("DELETE FROM medicament_info_table")
    suspend fun deleteAllMedicalInfo()

    @Query("SELECT * FROM medicament_info_table")
    fun readAllData(): LiveData<List<MedicamentInfo>>
}