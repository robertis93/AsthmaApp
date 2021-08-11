package com.rightname.asthmaapp.model

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
    var dateTimestamp: Long,
    var value: Int
) : Parcelable

@Parcelize
@Entity(tableName = "take_medicament_time_table")
data class TakeMedicamentTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var dateTimestamp: Long,
    @ColumnInfo(name = "medicamentInfo_id")
    var medicamentInfoId: String
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




