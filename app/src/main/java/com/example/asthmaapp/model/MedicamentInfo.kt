package com.example.asthmaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicament_info_table")
data class MedicamentInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var dose: Int,
)