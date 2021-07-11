package com.example.asthmaapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.MedicamentInfo

@Dao
interface MedicamentInfoDao {

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