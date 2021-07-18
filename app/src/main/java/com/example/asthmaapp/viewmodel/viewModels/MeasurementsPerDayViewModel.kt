package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.database.MeasureDataBase
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.viewmodel.repository.MeasureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MeasurementsPerDayViewModel(application: Application) : AndroidViewModel(application) {

    val date = MutableLiveData<String>()
    val measureListLiveData = MutableLiveData<List<Measure>>()
    val takeMedicamentTimeListLiveData = MutableLiveData<List<TakeMedicamentTimeEntity>>()
    val medicamentInfo = MutableLiveData<MedicamentInfo>()

    private val measureMutableList = mutableListOf<Measure>()
    private val takeMedicamentTimeMutableList = mutableListOf<TakeMedicamentTimeEntity>()

    //Вроде как нужно это вот!

    //  val measureList = MutableLiveData<List<Measure>>()
    // val takeMedicamentTimeList = MutableLiveData<List<TakeMedicamentTime>>()

    private val measureRepository: MeasureRepository

    init {
        val measurementsPerDayDao = MeasureDataBase.getDataBase(application).measurementsPerDayDao()
        measureRepository =
            MeasureRepository(measurementsPerDayDao)

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val currentDayTimeStamp = dateCalendar.time.time

        viewModelScope.launch {
            val medicament = measureRepository.getAllMedicamentInfoSync()
            if (medicament.size > 1) {
                val lastIndex = medicament.last()
                val medicamentNew =
                    MedicamentInfo(currentDayTimeStamp.toString(), lastIndex.name, lastIndex.dose)
                medicamentInfo.value = medicamentNew
            }
        }
    }


        val getAllMeasures: LiveData<List<Measure>> =
            measureRepository.getAllMeasures

        val takeMedicamentTimeGroupByDate: MutableLiveData<List<MeasureWithTakeMedicamentTime>> =
            MutableLiveData()

        val getAllMedicamentInfo: LiveData<List<MedicamentInfo>> =
            measureRepository.getAllMedicamentInfo

        fun getAllMeasuresAndTakeMedicamentTime() {
            viewModelScope.launch(Dispatchers.IO) {
                takeMedicamentTimeGroupByDate.postValue(measureRepository.getMeasureAndTakeMedicamentTimeGroupByDate())
            }
        }

        fun changeDate() {
            //   val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
//        currentDayTimeStamp = dateCalendar.time.time
//    val dateCalendar: Calendar =
//        GregorianCalendar(yearMeasure, monthMeasure, dayMeasure)
//
//    dateAfterChangedTimestamp = dateCalendar.time.time
        }

        fun save() {
            viewModelScope.launch(Dispatchers.IO) {
                for (measure in measureListLiveData.value ?: emptyList()) {
                    measureRepository.addMeasure(measure)
                }
                for (takeMedicamentTime in takeMedicamentTimeListLiveData.value ?: emptyList()) {
                    measureRepository.addTakeMedicamentTime(takeMedicamentTime)
                }
            }
        }


        fun addMeasure(measureDayTimeStamp: Long, measureWithPeakFlowMeter: Int) {
            Log.i("myLogs", "fun addMeasure ViewModel")
            //  viewModelScope.launch(Dispatchers.IO) {
            val measure =
                Measure(0, measureDayTimeStamp, measureWithPeakFlowMeter)


            measureMutableList.add(measure)
            measureListLiveData.value = measureMutableList
//            val newList =  measureListLiveData.value?.toMutableList()
//        newList?.let {
//            newList.add(measure)
//            measureListLiveData.value = it
            //  }

            // }

        }

        // TODO: когда нужен а когда не нужен viewModelScope.launch(Dispatchers.IO), что дает Value,
        //  решить вопрос чтобы сохранялись все значения, AddMeasure - не все элементф сохраняются,
        //  и последний не отображается элемент в ListFragment, разница возвращать livedate или список?? (время 1:10),
        //  разница между livedata и функцией через корутину и suspend
        fun addTakeMedicamentTime(
            medicamentDayTimeStamp: Long,
            currentDayTimeStamp: String
        ) {
            val medicamentTime =
                TakeMedicamentTimeEntity(
                    0,
                    medicamentDayTimeStamp,
                    currentDayTimeStamp.toString()
                )

            takeMedicamentTimeMutableList.add(medicamentTime)
            takeMedicamentTimeListLiveData.value = takeMedicamentTimeMutableList
        }

        fun deleteAllTimeTakeMedicament() {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteAllTimeTakeMedicament()
            }
        }

        fun updateMeasure(measure: Measure) {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.updateMeasure(measure)
            }
        }

        fun deleteMeasure(measure: Measure) {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteMeasure(measure)
            }
        }

        fun deleteAllMeasure() {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteAllMeasure()
            }
        }

        fun updateTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.updateTakeMedicamentTime(takeMedicamentTimeEntity)
            }
        }

        fun deleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteTakeMedicamentTime(takeMedicamentTimeEntity)
            }
        }

        fun deleteAllTakeMedicamentTime() {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteAllTakeMedicamentTime()
            }
        }

        fun addMedicamentInfo(medicamentInfo: MedicamentInfo) {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.addMedicamentInfo(medicamentInfo)
            }
        }

        fun updateMedicamentInfo(medicamentInfo: MedicamentInfo) {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.updateMedicamentInfo(medicamentInfo)
            }
        }

        fun deleteMedicamentInfo(medicamentInfo: MedicamentInfo) {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteMedicamentInfo(medicamentInfo)
            }
        }

        fun deleteAllMedicamentInfo() {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteAllMedicamentInfo()
            }
        }

        fun deleteAllMeasuresWithMedicaments() {
            viewModelScope.launch(Dispatchers.IO) {
                measureRepository.deleteAllMeasure()
                measureRepository.deleteAllTimeTakeMedicament()
                getAllMeasuresAndTakeMedicamentTime()
            }
        }
    }

