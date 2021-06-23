package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.databinding.ActivityAlarmTestBinding
import com.example.asthmaapp.databinding.TimeMedActivityAlarmTestBinding
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.models.MedicalInfo
import com.example.asthmaapp.view.adapters.AlarmActivityTestAdapter
import com.example.asthmaapp.view.adapters.ListAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel
import java.text.SimpleDateFormat
import java.util.*


class MedicTimeAlarmActivityTest : AppCompatActivity() {

    private lateinit var binding: TimeMedActivityAlarmTestBinding
    private lateinit var medicamentList: List<MedicalInfo>

    //инициализируем ViewModel ленивым способом
    // private val mMedicalViewModel by lazy { ViewModelProviders.of(this).get(MedicalViewModel::class.java)}
    private lateinit var mMedicalViewModel: MedicalViewModel
    private lateinit var mDayMeasureViewModel: MeasureOfDayViewModel

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //обрабатываем intent, если MainActivity открывается при нажатии на уведомление, то
        // переходит на AddFragment, если нет - то открывается MainActivity, соответственно проверяется через ntent,
        //если в нем что то есть или нет

        //подписываем адаптер на изменения списка
        // Get the view model
        // mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)


        // Create the observer which updates the ui
        //  val randomNumberObserver = Observer<MedicalInfo>{newNumber->
//            // Update the ui with current data
//            binding.timeAlarm.text = "Current Number : $newNumber"
//        }
//
//
//        mMedicalViewModel.readAllData.observe(this, Observer {
//            it?.let {
//                adapter.refreshUsers(it)
//            }
//        })
//        val dateTime = intent.getStringExtra("dateTime")
//
//        supportActionBar?.hide()
        binding = TimeMedActivityAlarmTestBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())
        mDayMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
//
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val dateMilli = dateCalendar.time.time
        val year = dateCalendar.time.year
        val month = dateCalendar.time.month
        val day = dateCalendar.time.day

        val timeMilli = dateCalendar.time.time
        val dateFormatTime = SimpleDateFormat("HH:mm")
        val dateFormatTimeMinute = SimpleDateFormat("mm")
        val dateFormatTimeHour = SimpleDateFormat("HH")
        val currentTime = Date(timeMilli)
        val ttime = dateFormatTime.format(currentTime)
        binding.timeAlarm.setText(ttime)

        //format date
        val currentDate = Date(dateMilli)
        val dateFormat = SimpleDateFormat("dd MMM YYYY")
        val ddate = dateFormat.format(currentDate)
        val new = Date(2021, 6, 23).getTime()


        val dateDayCalendar: Calendar = GregorianCalendar(year, month, day)
        val dayMilliId = dateDayCalendar.time.time
        // binding.textDate.setText(dayMilli.toString())
        binding.textDate.setText(ddate)
        val isCheck = binding.checkBox.onCheckIsTextEditor()


//            val measureOfDay = MeasureOfDay(idMed, dateMilli,
//        val id: String,
//        val day: Long,
//        val nameMedicament: String?,
//        val doza: Int?,
//        val frequency: Int?,


        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
        //заполняем поля Edit последними значениями из базы данных чтобы пользователь видел, что он принимает


        binding.saveBtn.setOnClickListener {
//            val index = adapterMeasure.getMedical().lastIndex
//            val nameMedicament = adapterMeasure.getMedical().get(index).nameOfMedicine
//            val frequenscyMed = adapterMeasure.getMedical().get(index).frequencyMedicine
//            val dozeMed = adapterMeasure.getMedical().get(index).doseMedicine
//        timeAndMeasureList.add(timeAndMeasure)
            val timeHour = dateFormatTimeHour.format(currentTime)
            val timeMinute = dateFormatTimeMinute.format(currentTime)


            val medicamentTime = MedicamentTime(
                0,
                timeHour.toInt(),
                timeMinute.toInt(),
                dateMilli,
                dayMilliId.toString(),
                isCheck
            )

            val infoDay = MeasureOfDay(
                dayMilliId.toString(),
                dateMilli,
                "aspirin",
                250,
                3
            )

            mDayMeasureViewModel.addMedicalTime(medicamentTime)
            mDayMeasureViewModel.addMeasure(infoDay)
            //выйти из приложения
            this.finishAffinity()
            // WorkManager.getInstance(applicationContext).cancelWorkById()
        }

    }
}