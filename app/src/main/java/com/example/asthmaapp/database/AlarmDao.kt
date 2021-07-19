package com.example.asthmaapp.database

import androidx.room.*
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.view.fragments.TypeAlarm

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

    @Query("Select * From alarm_table WHERE typeAlarm == :alarm ")
    suspend fun getAllAlarm(alarm : TypeAlarm): List<Alarm>
}