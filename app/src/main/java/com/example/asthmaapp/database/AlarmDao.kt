package com.example.asthmaapp.database

import androidx.room.*
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.view.fragments.TypeAlarm

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("Select * From alarm_table WHERE typeAlarm == :alarm ")
    suspend fun getListAlarm(alarm : TypeAlarm): List<Alarm>
}