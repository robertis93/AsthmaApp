package com.example.asthmaapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.models.MedicamentlInfo


//DAO (data access object)  отвечают за определение методов доступа к базе данных.
@Dao
interface MedicalInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMedicalInfo(medicamentlInfo: MedicamentlInfo)

    @Update
    suspend fun updateMedicalInfo(medicamentlInfo: MedicamentlInfo)

    @Delete
    suspend fun deleteMedicalInfo(medicamentlInfo: MedicamentlInfo)

    @Query("DELETE FROM medical_table")
    suspend fun deleteAllMedicalInfo()

    @Query("Select * From medical_table")
    fun readAllData(): LiveData<List<MedicamentlInfo>>

}