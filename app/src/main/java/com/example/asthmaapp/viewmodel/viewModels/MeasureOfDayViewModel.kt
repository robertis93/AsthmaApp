package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.model.*
import com.example.asthmaapp.model.database.MeasureDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MeasureOfDayViewModel(application: Application) : AndroidViewModel(application) {
    val measureOfDayLiveData = MutableLiveData<MeasureOfDay>()
    val timeMeasuresLiveData = MutableLiveData<List<TimeAndMeasure>>(emptyList())
    val timeMedicineLiveData = MutableLiveData<List<MedicamentTime>>(emptyList())

    private val medMeasureDao = MeasureDataBase.getDataBase(application).MedAndMeasureDao()
    private val medicalInfoDao = MeasureDataBase.getDataBase(application).medicalInfoDao()


    init {
        //создаем настояющую дату для ограничения по датам DatePicker
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val dayMilliseconds = dateCalendar.time.time
        val yearToday: Int = dateCalendar.get(Calendar.YEAR)
        val monthToday: Int = dateCalendar.get(Calendar.MONTH)
        val dayOfMonthToday: Int = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val justDayCalendar: Calendar =
            GregorianCalendar(yearToday, monthToday, dayOfMonthToday)
        var justDayMilli = justDayCalendar.time.time

        val idMed = justDayMilli.toString()

        viewModelScope.launch {
            val medicament = medicalInfoDao.readAllDataSync().last()
            val newMeasureOfDay = MeasureOfDay(
                idMed,
                dayMilliseconds,
                medicament.nameOfMedicine,
                medicament.doseMedicine,
                medicament.frequencyMedicine
            )
            measureOfDayLiveData.value = newMeasureOfDay
        }
        }


    //переделать на iveData
//    fun medicamentWithMeasures() {
//        viewModelScope.launch(Dispatchers.IO) {
//            medMeasureDao.getMedicamentAndMeasures()
//        }
//    }
//считываем все три списка
/*    val readAllMedicament: LiveData<List<MeasureOfDay>> = medMeasureDao.readAllMedicament()
    val readAllTimeAndMeasure: LiveData<List<TimeAndMeasure>> =
        medMeasureDao.readAllTimeAndMeasure()
    val readMedicamentAndMeasure: LiveData<List<MedWithMeasuresAndMedicamentTime>> =
        medMeasureDao.getMedicamentAndMeasures()*/

    fun changeDate(date: Long){
        measureOfDayLiveData.value = measureOfDayLiveData.value?.copy(
            day = date
        )
    }
    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val measureOfDay = measureOfDayLiveData.value
            if (measureOfDay != null) {
                medMeasureDao.insertMedicament(measureOfDay)
                for (measureTimes in timeMeasuresLiveData.value?: emptyList()) {
                    medMeasureDao.insertTimeAndMeasure(measureTimes)
                }
            }else{

            }
        }
    }

    fun addTimeAndMeasure(hour: Int, minute: Int, measurePicf: Int) {
        val timeAndMeasure = TimeAndMeasure(0, hour, minute, measurePicf, measureOfDayLiveData.value?.id ?:"")
        val newList = timeMeasuresLiveData.value?.toMutableList()
        newList?.let{
            newList.add(timeAndMeasure)
            timeMeasuresLiveData.value =it
        }
    }

    fun updateMeasure(measureOfDay: MeasureOfDay) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.updateMedicament(measureOfDay)
        }
    }

    fun deleteMeasure(measureOfDay: MeasureOfDay) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.deleteMedicament(measureOfDay)
        }
    }

    fun deleteAllMeasure() {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.deleteAllMeasure()
        }
    }

    fun updateTimeMeasure(timeAndMeasure: TimeAndMeasure) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.updateTimeAndMeasure(timeAndMeasure)
        }
    }

    fun deleteTimeMeasure(timeAndMeasure: TimeAndMeasure) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.deleteTimeAndMeasure(timeAndMeasure)
        }
    }

    fun deleteAllTimeMeasure() {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.deleteAllTimeAndMeasure()
        }
    }

    fun addMedicalTime(medicamentTime: MedicamentTime) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.addMedicalTime(medicamentTime)
        }
    }

    fun updateMedicalTime(medicamentTime: MedicamentTime) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.updateMedicalTime(medicamentTime)
        }
    }

    fun deleteMedicalTime(medicamentTime: MedicamentTime) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.deleteMedicalTime(medicamentTime)
        }
    }

    fun deleteAllMedicalTime() {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.deleteAllMedicalTime()
        }
    }
}





