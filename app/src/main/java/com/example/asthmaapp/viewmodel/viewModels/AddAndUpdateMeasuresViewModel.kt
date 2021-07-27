package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.DateUtil
import com.example.asthmaapp.utils.DateUtil.dateStringToDayTimeStamp
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddAndUpdateMeasuresViewModel(application: Application, val mode: Mode) :
    AndroidViewModel(application) {
    sealed class Mode {
        object Add : Mode()
        class Update(val measureWithTakeMedicamentTime: MeasureWithTakeMedicamentTime) : Mode()
    }

    val dateTimeStampLiveData = MutableLiveData<Long>()
    val dateLiveData = dateTimeStampLiveData.map { timestampToDisplayDate(it) }
    val measureListLiveData = MutableLiveData<List<Measure>>()
    val takeMedicamentTimeListLiveData = MutableLiveData<List<TakeMedicamentTimeEntity>>()
    val medicamentInfoLiveData = MutableLiveData<MedicamentInfo>()

    private val measureRepository: MeasureRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        measureRepository =
            MeasureRepository(measurementsPerDayDao)

        val dayTimeStamp = when (mode) {
            is Mode.Add -> GregorianCalendar(TimeZone.getTimeZone("GMT+5")).time.time
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
                val medicament = measureRepository.getAllMedicamentInfoSync()
                if (medicament.isNotEmpty())
                    medicament.last()
                else
                    null
            }
            is Mode.Update -> {
                val medicamentInfoList = mode.measureWithTakeMedicamentTime.takeMedicamentTimeList
                if (medicamentInfoList.isNotEmpty())
                    medicamentInfoList.first().medicamentInfo
                else
                    null
            }
        }
    }

    fun changeDate(newDate: Long) {
        dateTimeStampLiveData.value = newDate
    }

    fun getMaxDateForDialog(): Long {
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        return dateCalendar.time.time
    }

    suspend fun save(medicamentName: String, medicamentDose: String) {
        val medicamentInfo = MedicamentInfo(
            dateTimeStampLiveData.value.toString(),
            medicamentName,
            medicamentDose.toIntOrNull() ?: 0
        )
        measureRepository.addMedicamentInfo(medicamentInfo)
        for (measure in measureListLiveData.value ?: emptyList()) {
            measureRepository.addMeasure(measure)
        }
        for (takeMedicamentTime in takeMedicamentTimeListLiveData.value ?: emptyList()) {
            measureRepository.addTakeMedicamentTime(takeMedicamentTime)
        }
    }

    fun onAddMeasureClick(timeHour: Int, timeMinute: Int, measureWithPeakFlowMeter: Int) {
        val measureDayTimeStamp =
            DateUtil.dayTimeStamp(dateTimeStampLiveData.value!!, timeHour, timeMinute)
        val measure =
            Measure(0, measureDayTimeStamp, measureWithPeakFlowMeter)
        if (measureListLiveData.value?.map { measureDayTimeStamp}
                ?.contains(measureDayTimeStamp) == true)
        else {
            val measureMutableList = measureListLiveData.value?.toMutableList() ?: mutableListOf()
            measureMutableList.add(measure)
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
                dateTimeStampLiveData.value.toString()
            )
        if (takeMedicamentTimeListLiveData.value?.map { medicamentDayTimeStamp}
                ?.contains(medicamentDayTimeStamp) == true)
        else {
            val takeMedicamentTimeMutableList =
                takeMedicamentTimeListLiveData.value?.toMutableList() ?: mutableListOf()
            takeMedicamentTimeMutableList.add(medicamentTime)
            takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
        }
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
        takeMedicamentTimeMutableList[index].dateTimeStamp = medicamentTime.dateTimeStamp
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
            measureRepository.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
        }
        takeMedicamentTimeListLiveData.value?.let { listMedicamentTime ->
            val takeMedicamentTimeMutableList = listMedicamentTime.toMutableList()
            takeMedicamentTimeMutableList.remove(takeMedicamentTimeEntity)
            takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
        }
    }

    fun onDeleteMeasureClick(measure: Measure) {
        viewModelScope.launch(Dispatchers.IO) {
            measureRepository.deleteMeasure(measure)
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
    private val mode: AddAndUpdateMeasuresViewModel.Mode
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        AddAndUpdateMeasuresViewModel(application, mode) as T
}