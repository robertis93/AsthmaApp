package com.example.asthmaapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "measure_of_day_table")
data class MeasureOfDay(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    // val dayOfMeasure: String,
    val measureM: Int,
    val measureD: Int,
    val measureE: Int,
    val firstTime: String,
    val secondTime: String,
    val thirdTime: String
) : Parcelable
