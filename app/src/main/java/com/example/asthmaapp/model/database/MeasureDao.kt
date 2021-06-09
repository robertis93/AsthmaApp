 package com.example.asthmaapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asthmaapp.model.DayWithMeasures
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure

 @Dao
interface MeasureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMeasureOfDay(measureOfDay: MeasureOfDay)

     @Transaction
     @Query("SELECT * FROM measure_of_day_table")
     fun getDaysWithMeasures(): LiveData<List<DayWithMeasures>>

//     @Transaction
//     @Query("SELECT * FROM measure_of_day_table WHERE measureOfDayId = :measureOfDayId")
//     suspend fun getDaysWithMeasures(measureOfDayId : Int): List<DayWithMeasures>

    @Update
    suspend fun updateMeasure(measureOfDay: MeasureOfDay)

    @Delete
    suspend fun deleteMeasure(measureOfDay: MeasureOfDay)

    @Query("DELETE FROM measure_of_day_table")
    suspend fun deleteAllMeasure()


    @Query("Select * From measure_of_day_table")
    fun readAllData(): LiveData<List<MeasureOfDay>>
}


 //DAO (data access object)  отвечают за определение методов доступа к базе данных.
 @Dao
 interface TimeAndMeasureDao {

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun addTimeAndMeasure(timeAndMeasure: TimeAndMeasure)



     @Update
     suspend fun updateTimeAndMeasure(timeAndMeasure: TimeAndMeasure)

     @Delete
     suspend fun deleteTimeAndMeasure(timeAndMeasure: TimeAndMeasure)

     @Query("DELETE FROM time_measure_table")
     suspend fun deleteAllTimeAndMeasure()


     @Query("Select * From time_measure_table")
     fun readAllData(): LiveData<List<TimeAndMeasure>>

     @Query("Select * From time_measure_table")
     fun readNeedData(): LiveData<List<TimeAndMeasure>>

 }

 @Dao
 interface MedicalTimeDao {

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun addMedicalTime(medicamentTime: MedicamentTime)

     @Update
     suspend fun updateMedicalTime(medicamentTime: MedicamentTime)

     @Delete
     suspend fun deleteMedicalTime(medicamentTime: MedicamentTime)

     @Query("DELETE FROM medicament_time_table")
     suspend fun deleteAllMedicalTime()


     @Query("Select * From medicament_time_table ")
     fun readAllData(): LiveData<List<MedicamentTime>>

 }

// @Dao
// interface MeasureDaoTimeAndMeasureDao {
//
//     @Query("Select * From MeasureOfDayTimeAndMeasure")
//     fun readAllData(): LiveData<List<MeasureOfDayTimeAndMeasure>>
//
//     @Query("Select * From MeasureOfDayTimeAndMeasure WHERE idMeasureOfDay =:idMeasureOfDay")
//     fun readAllData(idMeasureOfDay): LiveData<List<MeasureOfDayTimeAndMeasure>>
//
// }