package com.example.asthmaapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.models.Alarm


//DAO (data access object)  отвечают за определение методов доступа к базе данных.
@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("DELETE FROM alarm_table")
    suspend fun deleteAllAlarms()


    @Query("Select * From alarm_table")
    fun readAllData(): LiveData<List<Alarm>>

}