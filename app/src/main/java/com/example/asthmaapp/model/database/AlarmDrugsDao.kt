package com.example.asthmaapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.DrugsAlarm


//DAO (data access object)  отвечают за определение методов доступа к базе данных.
@Dao
interface AlarmDrugsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(drugsAlarm : DrugsAlarm)

    @Update
    suspend fun updateAlarm(drugsAlarm : DrugsAlarm)

    @Delete
    suspend fun deleteAlarm(drugsAlarm : DrugsAlarm)

    @Query("DELETE FROM alarm_drugs_table")
    suspend fun deleteAllAlarms()


    @Query("Select * From alarm_drugs_table")
    fun readAllData(): LiveData<List<DrugsAlarm>>

}