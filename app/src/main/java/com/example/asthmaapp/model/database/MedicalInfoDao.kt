package com.example.asthmaapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.MedicalInfo


//DAO (data access object)  отвечают за определение методов доступа к базе данных.
@Dao
interface MedicalInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMedicalInfo(medicalInfo: MedicalInfo)

    @Update
    suspend fun updateMedicalInfo(medicalInfo: MedicalInfo)

    @Delete
    suspend fun deleteMedicalInfo(medicalInfo: MedicalInfo)

    @Query("DELETE FROM medical_table")
    suspend fun deleteAllMedicalInfo()


    @Query("Select * From medical_table")
    fun readAllData(): LiveData<List<MedicalInfo>>

}