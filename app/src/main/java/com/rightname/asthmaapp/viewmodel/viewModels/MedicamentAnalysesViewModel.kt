package com.rightname.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.rightname.asthmaapp.database.MeasureDataBase
import com.rightname.asthmaapp.model.Measure
import com.rightname.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.rightname.asthmaapp.model.MedicamentInfo
import com.rightname.asthmaapp.model.TakeMedicamentTimeEntity
import com.rightname.asthmaapp.utils.DateUtil
import com.rightname.asthmaapp.utils.DateUtil.dateStringToDayTimeStamp
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayHour
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayMinute
import com.rightname.asthmaapp.viewmodel.repository.MedicamentAnalysesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

class MedicamentAnalysesViewModel(application: Application, private val mode: Mode) :
    AndroidViewModel(application) {
    sealed class Mode : Serializable {
        object Add : Mode()
        class Update(val measureWithTakeMedicamentTime: MeasureWithTakeMedicamentTime) : Mode()
    }

    val dateTimeStampLiveData = MutableLiveData<Long>()
    val dateLiveData = dateTimeStampLiveData.map { timestampToDisplayDate(it) }
    val measureListLiveData = MutableLiveData<List<Measure>>()
    val takeMedicamentTimeListLiveData = MutableLiveData<List<TakeMedicamentTimeEntity>>()
    val medicamentInfoLiveData = MutableLiveData<MedicamentInfo>()

    private val medicamentAnalysesRepository: MedicamentAnalysesRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        val medicamentDao = MeasureDataBase.getDataBase(application).medicamentInfoDao()
        medicamentAnalysesRepository =
            MedicamentAnalysesRepository(measurementsPerDayDao, medicamentDao)

        val dayTimeStamp = when (mode) {
            is Mode.Add -> GregorianCalendar(TimeZone.getTimeZone("GMT")).time.time
            is Mode.Update -> dateStringToDayTimeStamp(mode.measureWithTakeMedicamentTime.date)
        }
        dateTimeStampLiveData.value = dayTimeStamp

        if (mode is Mode.Update) {
            measureListLiveData.value = mode.measureWithTakeMedicamentTime.measureList
            takeMedicamentTimeListLiveData.value =
                mode.measureWithTakeMedicamentTime.takeMedicamentTimeList.map { it.takeMedicamentTimeEntity }
        }
    }

    suspend fun getInitMedicamentInfo(): MedicamentInfo? {
        return when (mode) {
            is Mode.Add -> {
                val medicament = medicamentAnalysesRepository.getListMedicamentInfo()
                if (medicament.isNotEmpty())
                    medicament.last()
                else {
                    null
                }
            }
            is Mode.Update -> {
                val medicamentInfoList = mode.measureWithTakeMedicamentTime.takeMedicamentTimeList
                if (medicamentInfoList.isNotEmpty())
                    medicamentInfoList.first().medicamentInfo
                else {
                    null
                }
            }
        }
    }

    fun changeDate(newDate: Long) {
        dateTimeStampLiveData.value = newDate
    }

    fun getMaxDateForDialog(): Long {
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        return dateCalendar.time.time
    }

    suspend fun save(medicamentName: String, medicamentDose: String) {
        val medicamentInfo = MedicamentInfo(
            dateTimeStampLiveData.value.toString(),
            medicamentName,
            medicamentDose.toIntOrNull() ?: 0
        )
        if (mode is Mode.Update) {
            medicamentAnalysesRepository.updateMedicament(medicamentInfo)
        } else {
            medicamentAnalysesRepository.addMedicamentInfo(medicamentInfo)
        }
        for (measure in measureListLiveData.value ?: emptyList()) {
            val hour = timestampToDisplayHour(measure.dateTimestamp)
            val minute = timestampToDisplayMinute(measure.dateTimestamp)
            measure.dateTimestamp =
                DateUtil.dayTimeStamp(dateTimeStampLiveData.value!!, hour.toInt(), minute.toInt())
            medicamentAnalysesRepository.addMeasure(measure)
        }
        for (takeMedicamentTime in takeMedicamentTimeListLiveData.value ?: emptyList()) {
            val hour = timestampToDisplayHour(takeMedicamentTime.dateTimestamp)
            val minute = timestampToDisplayMinute(takeMedicamentTime.dateTimestamp)
            takeMedicamentTime.dateTimestamp =
                DateUtil.dayTimeStamp(dateTimeStampLiveData.value!!, hour.toInt(), minute.toInt())
            takeMedicamentTime.medicamentInfoId = dateTimeStampLiveData.value.toString()
            medicamentAnalysesRepository.addTakeMedicamentTime(takeMedicamentTime)
        }
    }

    fun onAddMeasureClick(timeHour: Int, timeMinute: Int, measureWithPeakFlowMeter: Int) {
        val measureDayTimeStamp =
            DateUtil.dayTimeStamp(dateTimeStampLiveData.value ?: 0, timeHour, timeMinute)
        val measure = Measure(0, measureDayTimeStamp, measureWithPeakFlowMeter)
        val measureMutableList = measureListLiveData.value?.toMutableList() ?: mutableListOf()
        measureMutableList.add(measure)
        measureListLiveData.value = measureMutableList
    }

    fun onAddTakeMedicamentTimeClick(timeHour: Int, timeMinute: Int) {
        val medicamentDayTimeStamp =
            DateUtil.dayTimeStamp(dateTimeStampLiveData.value ?: 0, timeHour, timeMinute)
        val medicamentTime =
            TakeMedicamentTimeEntity(
                0,
                medicamentDayTimeStamp,
                dateTimeStampLiveData.value.toString()
            )
        val takeMedicamentTimeMutableList =
            takeMedicamentTimeListLiveData.value?.toMutableList() ?: mutableListOf()
        takeMedicamentTimeMutableList.add(medicamentTime)
        takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
    }

    fun onUpdateTakeMedicamentTimeClick(
        index: Int,
        timeHour: Int,
        timeMinute: Int
    ) {
        val medicamentDayTimeStamp =
            DateUtil.dayTimeStamp(dateTimeStampLiveData.value ?: 0, timeHour, timeMinute)
        val medicamentTime =
            TakeMedicamentTimeEntity(
                0,
                medicamentDayTimeStamp,
                dateTimeStampLiveData.value.toString()
            )
        val takeMedicamentTimeMutableList =
            takeMedicamentTimeListLiveData.value?.toMutableList() ?: mutableListOf()
        takeMedicamentTimeMutableList[index].dateTimestamp = medicamentTime.dateTimestamp
        takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
    }

    fun onUpdateMeasureClick(
        index: Int,
        timeHour: Int,
        timeMinute: Int,
        measureWithPeakFlowMeter: Int
    ) {
        val measureDayTimeStamp =
            DateUtil.dayTimeStamp(dateTimeStampLiveData.value ?: 0, timeHour, timeMinute)
        val measure =
            Measure(0, measureDayTimeStamp, measureWithPeakFlowMeter)
        val measureMutableList = measureListLiveData.value?.toMutableList() ?: mutableListOf()
        measureMutableList[index].value = measure.value
        measureMutableList[index].dateTimestamp = measure.dateTimestamp
        measureListLiveData.value = measureMutableList
    }

    fun onDeleteMedicamentClick(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            medicamentAnalysesRepository.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
        }
        takeMedicamentTimeListLiveData.value?.let { listMedicamentTime ->
            val takeMedicamentTimeMutableList = listMedicamentTime.toMutableList()
            takeMedicamentTimeMutableList.remove(takeMedicamentTimeEntity)
            takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
        }
    }

    fun onDeleteMeasureClick(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            medicamentAnalysesRepository.deleteMeasure(measure)
        }
        measureListLiveData.value?.let { listMeasure ->
            val measureMutableList = listMeasure.toMutableList()
            measureMutableList.remove(measure)
            measureListLiveData.value = measureMutableList
        }
    }
}

class AddAndUpdateMeasureViewModelFactory(
    private val application: Application,
    private val mode: MedicamentAnalysesViewModel.Mode
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MedicamentAnalysesViewModel(application, mode) as T
}