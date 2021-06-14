package com.example.asthmaapp.viewmodel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.asthmaapp.model.database.MeasureDataBase
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedWithMeasures
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeasureOfDayViewModel(application: Application) : AndroidViewModel(application) {
    private val medMeasureDao = MeasureDataBase.getDataBase(application).MedAndMeasureDao()

    //переделать на iveData
//    fun medicamentWithMeasures() {
//        viewModelScope.launch(Dispatchers.IO) {
//            medMeasureDao.getMedicamentAndMeasures()
//        }
//    }
//считываем все три списка
    val readAllMedicament: LiveData<List<MeasureOfDay>> = medMeasureDao.readAllMedicament()
    val readAllTimeAndMeasure: LiveData<List<TimeAndMeasure>> = medMeasureDao.readAllTimeAndMeasure()
    val readMedicamentAndMeasure: LiveData<List<MedWithMeasures>> = medMeasureDao.getMedicamentAndMeasures()

    fun addMeasure(measureOfDay: MeasureOfDay) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.insertMedicament(measureOfDay)
        }
    }

    fun addTimeAndMeasure(timeAndMeasure: TimeAndMeasure) {
        viewModelScope.launch(Dispatchers.IO) {
            medMeasureDao.insertTimeAndMeasure(timeAndMeasure)
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










//    fun addTimeAndMeasure(timeAndMeasure: TimeAndMeasure) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val resultId = repositoryMeasure.addTimeMeasure(timeAndMeasure)
//            if (resultId >= 0) {
//                // TODO: handle success
//            } else {
//                // TODO: handle error
//            }
//        }
//    }




