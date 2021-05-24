package com.example.asthmaapp.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var hour: Int,
    var minute: Int,
)