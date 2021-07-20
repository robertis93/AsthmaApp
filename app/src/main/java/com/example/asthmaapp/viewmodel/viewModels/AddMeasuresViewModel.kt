package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.DateUtil
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddMeasuresViewModel(application: Application) : AndroidViewModel(application) {
    val dateTimeStampLiveData = MutableLiveData<Long>()
    val dateLiveData = dateTimeStampLiveData.map { timestampToDisplayDate(it) }
    val measureListLiveData = MutableLiveData<List<Measure>>()
    val takeMedicamentTimeListLiveData = MutableLiveData<List<TakeMedicamentTimeEntity>>()
    val medicamentInfoLiveData = MutableLiveData<MedicamentInfo>()

    private val measureMutableList = mutableListOf<Measure>()
    private val takeMedicamentTimeMutableList = mutableListOf<TakeMedicamentTimeEntity>()
    private val measureRepository: MeasureRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        measureRepository =
            MeasureRepository(measurementsPerDayDao)

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val currentDayTimeStamp = dateCalendar.time.time
        dateTimeStampLiveData.value = currentDayTimeStamp

        getLastMedicament(currentDayTimeStamp)
    }

    private fun getLastMedicament(currentDayTimeStamp: Long) {
        viewModelScope.launch {
            val medicament = measureRepository.getAllMedicamentInfoSync()
            if (medicament.isNotEmpty()) {
                val lastIndex = medicament.last()
                val medicamentNew =
                    MedicamentInfo(currentDayTimeStamp.toString(), lastIndex.name, lastIndex.dose)
                medicamentInfoLiveData.value = medicamentNew
            }
        }
    }

    fun changeDate(newDate: Long) {
        dateTimeStampLiveData.value = newDate
    }

    fun changeMedicamentName(nameMedicament: String) {
        medicamentInfoLiveData.value?.name = nameMedicament
    }

    fun changeMedicamentDose(doseMedicament: String) {
        if (doseMedicament == "") {
            medicamentInfoLiveData.value?.dose = 0
        } else
            medicamentInfoLiveData.value?.dose = doseMedicament.toInt()
    }

    fun getMaxDateForDialog(): Long {
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        return dateCalendar.time.time
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            for (measure in measureListLiveData.value ?: emptyList()) {
                measureRepository.addMeasure(measure)
            }
            for (takeMedicamentTime in takeMedicamentTimeListLiveData.value ?: emptyList()) {
                measureRepository.addTakeMedicamentTime(takeMedicamentTime)
            }
            medicamentInfoLiveData.value?.let { measureRepository.addMedicamentInfo(it) }
        }
    }


    fun onAddMeasureClick(timeHour: Int, timeMinute: Int, measureWithPeakFlowMeter: Int) {
        val measureDayTimeStamp =
            DateUtil.dayTimeStamp(dateTimeStampLiveData.value!!, timeHour, timeMinute)
        val measure =
            Measure(0, measureDayTimeStamp, measureWithPeakFlowMeter)
        measureMutableList.add(measure)
        measureListLiveData.value = measureMutableList
    }

    fun onDeleteMeasureClick(measure: Measure) {
        measureListLiveData.value?.let { listMeasure ->
            val measureMutableList = listMeasure.toMutableList()
            measureMutableList.remove(measure)
            measureListLiveData.value = measureMutableList
        }
    }

    fun onAddTakeMedicamentTimeClick(timeHour: Int, timeMinute: Int) {
        val medicamentDayTimeStamp =
            DateUtil.dayTimeStamp(dateTimeStampLiveData.value ?: 0, timeHour, timeMinute)
        val medicamentTime =
            TakeMedicamentTimeEntity(
                0,
                medicamentDayTimeStamp,
                medicamentInfoLiveData.value?.id!!
            )
        takeMedicamentTimeMutableList.add(medicamentTime)
        takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
    }

    fun onDeleteMedicamentClick(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        takeMedicamentTimeListLiveData.value?.let { listMedicamentTime ->
            val takeMedicamentTimeMutableList = listMedicamentTime.toMutableList()
            takeMedicamentTimeMutableList.remove(takeMedicamentTimeEntity)
            takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
        }
    }


}

