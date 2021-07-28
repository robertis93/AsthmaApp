package com.example.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.asthmaapp.database.InformationPerDayDao
import com.example.asthmaapp.model.*
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayDate

typealias TakeMedicamentTimeGroupByDate = Map<String, List<TakeMedicamentTime>>

class InformationPerDayRepository(private val informationPerDayDao: InformationPerDayDao) {

    val getAllMeasures: LiveData<List<Measure>> =
        informationPerDayDao.readAllMeasure()

    suspend fun getListMedicamentInfo(): List<MedicamentInfo> =
        informationPerDayDao.getListMedicamentInfo()

    fun getMeasureAndTakeMedicamentTimeGroupByDate(): List<MeasureWithTakeMedicamentTime> {
        val measureGroup = informationPerDayDao.getListMeasure()
            .groupBy {
                timestampToDisplayDate(it.dateTimestamp)
            }

        val takeMedicamentTimeGroup = informationPerDayDao.getListTakeMedicamentTime()
            .groupBy {
                timestampToDisplayDate(it.takeMedicamentTimeEntity.dateTimestamp)
            }

        return (measureGroup.keys + takeMedicamentTimeGroup.keys).map { date ->
            val measures = measureGroup[date] ?: emptyList()
            val takeMedicamentTimeList = takeMedicamentTimeGroup[date] ?: emptyList()
            MeasureWithTakeMedicamentTime(date, measures, takeMedicamentTimeList)
        }
            .reversed()
    }

    suspend fun addMeasure(measure: Measure) {
        informationPerDayDao.insertMeasure(measure)
    }

    suspend fun deleteAllTimeTakeMedicament() {
        informationPerDayDao.deleteAllTakeMedicamentTime()
    }

    suspend fun deleteMeasure(measure: Measure) {
        informationPerDayDao.deleteMeasure(measure)
    }

    suspend fun deleteAllMeasure() {
        informationPerDayDao.deleteAllMeasure()
    }

    suspend fun addTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        informationPerDayDao.addTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun deleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        informationPerDayDao.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun addMedicamentInfo(medicamentInfo: MedicamentInfo) {
        informationPerDayDao.addMedicamentInfo(medicamentInfo)
    }

    suspend fun updateMedicament(medicamentInfo: MedicamentInfo) {
        informationPerDayDao.updateMedicamentInfo(medicamentInfo)
    }
}
