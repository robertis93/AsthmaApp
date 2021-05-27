package com.example.asthmaapp.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_drugs_table")
data class DrugsAlarm(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var hour: Int,
    var minute: Int,
)