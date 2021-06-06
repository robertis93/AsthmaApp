package com.example.asthmaapp.model.models

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.TimeAndMeasure

//
//@Entity (foreignKeys = [
//ForeignKey(entity = MeasureOfDay::class, parentColumns = ["id"], childColumns = ["idMeasureOfDay"]),
//ForeignKey(entity = TimeAndMeasure::class, parentColumns = ["id"], childColumns = ["idTimeAndMeasure"] )])
//data class MeasureOfDayTimeAndMeasure(
//    val idMeasureOfDay : Int,
//    val idTimeAndMeasure : Int)