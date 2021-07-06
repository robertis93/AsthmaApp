package com.example.asthmaapp.model.models

import android.hardware.ConsumerIrManager
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "medical_table")
data class MedicamentlInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var nameOfMedicine: String,
    var doseMedicine: Int,
)