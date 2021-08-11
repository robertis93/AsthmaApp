package com.rightname.asthmaapp.viewmodel.repository

import androidx.lifecycle.LiveData
import com.rightname.asthmaapp.database.MedicamentAnalysesDao
import com.rightname.asthmaapp.database.MedicamentInfoDao
import com.rightname.asthmaapp.model.*
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayDate

typealias TakeMedicamentTimeGroupByDate = Map<String, List<TakeMedicamentTime>>

class MedicamentAnalysesRepository(private val medicamentAnalysesDao: MedicamentAnalysesDao, private val medicamentInfoDao: MedicamentInfoDao) {

    val getAllMeasures: LiveData<List<Measure>> =
        medicamentAnalysesDao.readAllMeasure()

    suspend fun getListMedicamentInfo(): List<MedicamentInfo> =
        medicamentAnalysesDao.getListMedicamentInfo()

    fun getMeasureAndTakeMedicamentTimeGroupByDate(): List<MeasureWithTakeMedicamentTime> {
        val measureGroup = medicamentAnalysesDao.getListMeasure()
            .groupBy {
                timestampToDisplayDate(it.dateTimestamp)
            }

        val takeMedicamentTimeGroup = medicamentAnalysesDao.getListTakeMedicamentTime()
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
        medicamentAnalysesDao.insertMeasure(measure)
    }

    suspend fun deleteAllTimeTakeMedicament() {
        medicamentAnalysesDao.deleteAllTakeMedicamentTime()
    }

    suspend fun deleteMeasure(measure: Measure) {
        medicamentAnalysesDao.deleteMeasure(measure)
    }

    suspend fun deleteAllMeasure() {
        medicamentAnalysesDao.deleteAllMeasure()
    }

    suspend fun addTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        medicamentAnalysesDao.addTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun deleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        medicamentAnalysesDao.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
    }

    suspend fun addMedicamentInfo(medicamentInfo: MedicamentInfo) {
        medicamentInfoDao.addMedicamentInfo(medicamentInfo)
    }

    suspend fun updateMedicament(medicamentInfo: MedicamentInfo) {
        medicamentInfoDao.updateMedicamentInfo(medicamentInfo)
    }
}
