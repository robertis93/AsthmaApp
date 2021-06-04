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
    val dayOfMeasure: String,
    val measureOne: Int?,
    val measureTwo: Int?,
    val measureThree: Int?,
    val measureFour: Int?,
    val measureFive: Int?,
    val measureSix: Int?,
    val firstTime: String?,
    val secondTime: String?,
    val thirdTime: String?,
    val fourthTime: String?,
    val fifthTime: String?,
    val sixTime: String?
) : Parcelable
