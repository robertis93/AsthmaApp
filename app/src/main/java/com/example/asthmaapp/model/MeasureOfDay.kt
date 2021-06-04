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
    //val listOfTimeMeasure: List<TimeAndMeasure>,
  //  val listOfTimeOfMedicament: List<MedicamentTime>,
    val nameMedicamentaion: String?,
    val doza : Int?,
    val frequency : Int?,
//сюда нужно будет добавить список из TimeAndMeasure
) : Parcelable

@Entity(tableName = "time_measure_table")
data class TimeAndMeasure(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var hour: Int,
    var minute: Int,
    var measure : Int
)

@Entity(tableName = "medicament_time_table")
data class MedicamentTime(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var hour: Int,
    var minute: Int,
    var check: Boolean
)
