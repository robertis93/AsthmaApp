package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.asthmaapp.database.MeasurementsPerDayDao
import com.example.asthmaapp.model.*
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatDayMonthYear

typealias TakeMedicamentTimeGroupByDate = Map<String, List<TakeMedicamentTime>>

class MeasureRepository(private val measurementsPerDayDao: MeasurementsPerDayDao) {

    val getAllMeasures: LiveData<List<Measure>> =
        measurementsPerDayDao.readAllMeasure()

    val getAllMedicamentInfo: LiveData<List<MedicamentInfo>> =
        measurementsPerDayDao.readAllMedicamentInfo()

    fun getMeasureAndTakeMedicamentTimeGroupByDate(): List<MeasureWithTakeMedicamentTime> {
        val measureGroup = measurementsPerDayDao.getListMeasure()
            .groupBy {
                val date =
                    dateTimeStampToSimpleDateFormatDayMonthYear(it.dateTimestamp)
                date
            }

        val takeMedicamentTimeGroup = measurementsPerDayDao.getListTakeMedicamentTime()
            .groupBy {
                val date =
                    dateTimeStampToSimpleDateFormatDayMonthYear(it.takeMedicamentTimeEntity.dateTimeStamp)
                date
            }

        return (measureGroup.keys + takeMedicamentTimeGroup.keys).map { date ->
            val measures = measureGroup[date] ?: emptyList()
            val takeMedicamentTimeList = takeMedicamentTimeGroup[date] ?: emptyList()
            MeasureWithTakeMedicamentTime(date, measures, takeMedicamentTimeList)
        }
            .reversed()
//            .sortedBy { it.date /*TODO: get timestamp from date string*/ }
    }

    suspend fun addMeasure(measure: Measure) {
        measurementsPerDayDao.insertMeasure(measure)
    }

    suspend fun deleteAllTimeTakeMedicament() {
        measurementsPerDayDao.deleteAllTimeTakeMedicament()
    }

    suspend fun updateMeasure(measure: Measure) {
        measurementsPerDayDao.updateMeasure(measure)
    }

    suspend fun deleteMeasure(measure: Measure) {
        measurementsPerDayDao.deleteMeasure(measure)
    }

    suspend fun deleteAllMeasure() {
        measurementsPerDayDao.deleteAllMeasure()
    }

    suspend fun addTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measurementsPerDayDao.addTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun updateTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measurementsPerDayDao.updateTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun deleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measurementsPerDayDao.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun deleteAllTakeMedicamentTime() {
        measurementsPerDayDao.deleteTakeMedicamentTime()
    }

    suspend fun addMedicamentInfo(medicamentInfo: MedicamentInfo) {
        measurementsPerDayDao.addMedicamentInfo(medicamentInfo)
    }

    suspend fun updateMedicamentInfo(medicamentInfo: MedicamentInfo) {
        measurementsPerDayDao.updateMedicamentInfo(medicamentInfo)
    }

    suspend fun deleteMedicamentInfo(medicamentInfo: MedicamentInfo) {
        measurementsPerDayDao.deleteMedicamentInfo(medicamentInfo)
    }

    suspend fun deleteAllMedicamentInfo() {
        measurementsPerDayDao.deleteAllMedicamentInfo()
    }
}
