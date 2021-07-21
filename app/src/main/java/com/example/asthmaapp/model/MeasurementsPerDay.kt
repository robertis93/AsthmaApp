package com.example.asthmaapp.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "measure_table"
)
data class Measure(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dateTimestamp: Long,
    var measure: Int
) : Parcelable

@Parcelize
@Entity(tableName = "time_take_medicament_table")
data class TakeMedicamentTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dateTimeStamp: Long,
    @ColumnInfo(name = "medicamentInfo_id")
    val medicamentInfoId: String
) : Parcelable

@Parcelize
@Entity(tableName = "medicament_info_table")
data class MedicamentInfo(
    @PrimaryKey
    val id: String,
    var name: String,
    var dose: Int
) : Parcelable

@Parcelize
data class TakeMedicamentTime(
    @Embedded
    val takeMedicamentTimeEntity: TakeMedicamentTimeEntity,
    @Relation(
        parentColumn = "medicamentInfo_id",
        entityColumn = "id"
    )
    val medicamentInfo: MedicamentInfo
) : Parcelable

@Parcelize
data class MeasureWithTakeMedicamentTime(
    val date: String,
    val measureList: List<Measure>,
    val takeMedicamentTimeList: List<TakeMedicamentTime>,
) : Parcelable




