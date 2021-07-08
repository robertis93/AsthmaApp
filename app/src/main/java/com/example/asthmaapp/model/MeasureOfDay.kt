package com.example.asthmaapp.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
// TODO: rename to day_measure_table
@Entity(tableName = "medicament_day_table")
// TODO: rename to DayMeasure
data class MeasureOfDay(
    @PrimaryKey
    val id: String,
    // TODO: rename to dateTimestamp
    val day: Long,
    val nameMedicament: String,
    val doseMedicament: Int?
) : Parcelable

@Parcelize
@Entity(
    tableName = "time_measure_table"
)
// TODO: rename to Measure
data class TimeAndMeasure(
    @PrimaryKey(autoGenerate = true)
    // TODO: rename to id
    var idMeasure: Int,
    // TODO: rename to dayMeasureId
    var idMed: String,
    var hour: Int,
    var minute: Int,
    var measure: Int
) : Parcelable

@Parcelize
@Entity(tableName = "medicament_time_table")
// TODO: remove all "med", "medical", "drugs", etc...
data class MedicamentTime(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var hour: Int,
    var minute: Int,
    // TODO: remove ???
    val day: Long,
    // TODO: rename to dayMeasureId
    var idMed: String
) : Parcelable


@Parcelize
data class MedWithMeasuresAndMedicamentTime(
    @Embedded val day: MeasureOfDay,
    @Relation(
        parentColumn = "id",
        entityColumn = "idMed"
    )
    val measures: List<TimeAndMeasure>,
    @Relation(
        parentColumn = "id",
        entityColumn = "idMed"
    )
    val medicamentTime: List<MedicamentTime>
) : Parcelable


