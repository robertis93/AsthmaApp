package com.example.asthmaapp.model

import android.os.Parcelable
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = "measure_of_day_table")
data class MeasureOfDay(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dayOfMeasure: String,
    @TypeConverters(ListOfTimeMeasureConverters::class)
    var listOfTimeMeasure: @RawValue List<TimeAndMeasure>,
    val nameMedicamentaion: String?,
    val doza: Int?,
    val frequency: Int?,
) : Parcelable

class ListOfTimeMeasureConverters {
    @TypeConverter
    fun fromString(json: String): List<TimeAndMeasure> {
        val listType = object : TypeToken<List<TimeAndMeasure>>() {}.type
        return Gson().fromJson<List<TimeAndMeasure>>(json, listType)
    }

    @TypeConverter
    fun frmList(list: List<TimeAndMeasure>): String {
        return Gson().toJson(list)
    }
}

@Entity(tableName = "time_measure_table")
data class TimeAndMeasure(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    // var dayTimeAndMeasure : String,
    var hour: Int,
    var minute: Int,
    var measure: Int,
    //  @ColumnInfo(name = "measure_of_day_id")
    // var measureOofDdayId: Int
)


@Entity(tableName = "medicament_time_table")
data class MedicamentTime(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var hour: Int,
    var minute: Int,
    var check: Boolean,
//    @ColumnInfo(name = "measure_of_day_id")
//    var measureOofDdayId: Int
)

