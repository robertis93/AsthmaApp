package com.example.asthmaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.asthmaapp.view.fragments.TypeAlarm

@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey
    var id: String,
    var hour: Int,
    var minute: Int,
    var typeAlarm : TypeAlarm
)