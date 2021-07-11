package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.asthmaapp.database.MeasurementsPerDayDao
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.millisecondsToStringDateDayMonthYear
import com.example.asthmaapp.viewmodel.viewModels.TakeMedicamentTimeGroupByDate

class MeasureRepository(private val measurementsPerDayDao: MeasurementsPerDayDao) {

    val readAllMeasure: LiveData<List<Measure>> =
        measurementsPerDayDao.readAllMeasure()

    val takeMedicamentTimeGroupByDate: LiveData<TakeMedicamentTimeGroupByDate> =
        measurementsPerDayDao.getAllTakeMedicamentTime()
            .map {
                it.groupBy { takeMedicamentTime ->
                    val date =
                        millisecondsToStringDateDayMonthYear(takeMedicamentTime.takeMedicamentTime.dateTimestamp)
                    date
                }
            }

    fun getMeasureAndTakeMedicamentTimeGroupByDate() : List<MeasureWithTakeMedicamentTime> {
        val measureGroup = measurementsPerDayDao.getAllMeasureList()
            .groupBy {
                val date =
                    millisecondsToStringDateDayMonthYear(it.dateTimestamp)
                date
            }
//            .map {
//                val date = millisecondsToStringDate(it.dateTimestamp)
                // TODO: create and use Builder pattern
//                MeasureWithTakeMedicamentTime(date, it, null)
//            }

        val takeMedicamentTimeGroup = measurementsPerDayDao.getAllTakeMedicamentTimeList()
            .groupBy {
                val date =
                    millisecondsToStringDateDayMonthYear(it.takeMedicamentTime.dateTimestamp)
                date
            }
//            .map {
//                val date = millisecondsToStringDate(it.takeMedicamentTime.dateTimestamp)
//                // TODO: create and use Builder pattern
//                MeasureWithTakeMedicamentTime(date, it, null)
//            }

//        val result = mutableListOf<MeasureWithTakeMedicamentTime>()
//        measureGroup.mapTo(result) { (date, measures) ->
//            val takeMedicamentTimeList = takeMedicamentTimeGroup[date]
//            return MeasureWithTakeMedicamentTime(date, measures, takeMedicamentTimeList)
//        }

        return (measureGroup.keys + takeMedicamentTimeGroup.keys).map { date ->
            val measures = measureGroup[date] ?: emptyList()
            val takeMedicamentTimeList = takeMedicamentTimeGroup[date] ?: emptyList()
            MeasureWithTakeMedicamentTime(date, measures, takeMedicamentTimeList)
        }
//        measureList.


//            .groupBy { takeMedicamentTime ->
//                val date =
//                    millisecondsToStringDate(takeMedicamentTime.takeMedicamentTime.dateTimestamp)
//                date
//            }
    }

    suspend fun addTimeAndMeasure(measure: Measure) {
        measurementsPerDayDao.insertMeasure(measure)
    }

    suspend fun deleteAllMeasure() {
        measurementsPerDayDao.deleteAllTimeTakeMedicament()
    }

    suspend fun updateTimeMeasure(measure: Measure) {
        measurementsPerDayDao.updateMeasure(measure)
    }

    suspend fun deleteTimeMeasure(measure: Measure) {
        measurementsPerDayDao.deleteMeasure(measure)
    }

    suspend fun deleteAllTimeMeasure() {
        measurementsPerDayDao.deleteAllMeasure()
    }

    suspend fun addMedicalTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measurementsPerDayDao.addTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun updateMedicalTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measurementsPerDayDao.updateTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun deleteMedicalTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measurementsPerDayDao.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun deleteAllMedicalTime() {
        measurementsPerDayDao.deleteTakeMedicamentTime()
    }

    val readAllData: LiveData<List<MedicamentInfo>> =  measurementsPerDayDao.readAllMedicamentInfo()

    suspend fun addMedicalInfo(medicamentInfo: MedicamentInfo) {
        measurementsPerDayDao.addMedicamentInfo(medicamentInfo)
    }

    suspend fun updateMedicalInfo(medicamentInfo: MedicamentInfo) {
        measurementsPerDayDao.updateMedicamentInfo(medicamentInfo)
    }

    suspend fun deleteMedicalInfo(medicamentInfo: MedicamentInfo) {
        measurementsPerDayDao.deleteMedicamentInfo(medicamentInfo)
    }

    suspend fun deleteAllMedicalInfo() {
        measurementsPerDayDao.deleteAllMedicamentInfo()
    }
}
