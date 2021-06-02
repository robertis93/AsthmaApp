package com.example.asthmaapp.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey
    var id: String,
    var hour: Int,
    var minute: Int,
)