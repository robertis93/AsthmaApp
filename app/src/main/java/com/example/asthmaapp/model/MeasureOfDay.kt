package com.example.asthmaapp.model

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "medicament_day_table")
data class MeasureOfDay(
    // = class MedicalInfo
    @PrimaryKey
    val id: String,
    val day: Long,
    val nameMedicament: String?,
    val doza: Int?,
    val frequency: Int?,
) : Parcelable

@Parcelize
@Entity(
    tableName = "time_measure_table"
)
data class TimeAndMeasure(
    @PrimaryKey(autoGenerate = true)
    var idMeasure: Int,
    var hour: Int,
    var minute: Int,
    var measure: Int,
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

@Parcelize
@Entity(tableName = "medicament_time_table")
data class MedicamentTime(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var hour: Int,
    var minute: Int,
    val day: Long,
    var idMed: String,
    var check: Boolean,
//    @ColumnInfo(name = "measure_of_day_id")
//    var measureOofDdayId: Int
) : Parcelable


