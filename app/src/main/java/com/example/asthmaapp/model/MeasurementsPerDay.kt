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
    val timeHour: Int,
    val timeMinute: Int,
    val measure: Int
) : Parcelable

@Parcelize
@Entity(tableName = "time_take_medicament_table")
data class TakeMedicamentTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dateTimestamp: Long,
    val timeHour: Int,
    val timeMinute: Int,
    @ColumnInfo(name = "medicamentInfo_id")
    val medicamentInfoId: Int
) : Parcelable

@Parcelize
@Entity(tableName = "medicament_info_table")
data class MedicamentInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val dose: Int
) : Parcelable

@Parcelize
data class TakeMedicamentTime(
    @Embedded
    val takeMedicamentTime: TakeMedicamentTimeEntity,
    @Relation(
        parentColumn = "medicamentInfo_id",
        entityColumn = "id"
    )
    val medicamentInfo: MedicamentInfo
) : Parcelable

@Parcelize
data class MeasureWithTakeMedicamentTime(
    val date: String, // create from dateTimestamp
    val measureList: List<Measure>,
    val takeMedicamentTimeList: List<TakeMedicamentTime>,
) : Parcelable

/*
@Parcelize
data class TakeMedicamentTime(
    @Embedded
    val medicamentInfo: MedicamentInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "medicamentInfo_id"
    )
    val takeMedicamentTime: List<TakeMedicamentTimeEntity>
) : Parcelable
*/



