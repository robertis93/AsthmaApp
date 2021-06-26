package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.databinding.ActivityAlarmTestBinding
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.models.MedicalInfo
import com.example.asthmaapp.view.adapters.AlarmActivityTestAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel
import java.text.SimpleDateFormat
import java.util.*


class AlarmActivityTest : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmTestBinding
    private lateinit var medicamentList: List<MedicalInfo>

    //инициализируем ViewModel ленивым способом
    // private val mMedicalViewModel by lazy { ViewModelProviders.of(this).get(MedicalViewModel::class.java)}
    private lateinit var mMedicalViewModel: MedicalViewModel
    private lateinit var mDayMeasureViewModel: MeasureOfDayViewModel
    private var medicalInfo = mutableListOf<MedicalInfo>()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmTestBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

        supportActionBar?.hide()
        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
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


        val dateDayCalendar: Calendar = GregorianCalendar(year, month, day)
        val dayMilliId = dateDayCalendar.time.time
        // binding.textDate.setText(dayMilli.toString())
        binding.textDate.setText(ddate)



        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
        //заполняем поля Edit последними значениями из базы данных чтобы пользователь видел, что он принимает

        //recycler
        val adapterMeasure = AlarmActivityTestAdapter()
        val recyclerView = binding.recyclerMeasure
        recyclerView.adapter = adapterMeasure
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        mMedicalViewModel.readAllData.observe(this) { measure ->
            // Update the cached copy of the words in the adapter.
            adapterMeasure.addMedicalInfo(measure)
            medicalInfo.addAll(measure)
            Log.i("myLogs", "adapterMeasure.addMedicalInfo(measure)")
        }

        Log.i("myLogs", "binding.measure.setText(adapterMeasure.getMedical().toString())")



        binding.saveBtn.setOnClickListener {
            val timeHour = dateFormatTimeHour.format(currentTime)
            val timeMinute = dateFormatTimeMinute.format(currentTime)
            binding.measure.setFocusableInTouchMode(true);
            binding.measure.requestFocus();
            val measurePicf = binding.measure.text.toString().toInt()
            val idMed = UUID.randomUUID().toString()
            //  val idMed
            val timeAndMeasure =
                TimeAndMeasure(
                    0,
                    timeHour.toInt(),
                    timeMinute.toInt(),
                    measurePicf,
                    dayMilliId.toString()
                )

            val index = medicalInfo.lastIndex
            val nameMed = medicalInfo.get(index).nameOfMedicine
            val dozaMed = medicalInfo.get(index).doseMedicine
            val freqMed = medicalInfo.get(index).frequencyMedicine

            val infoDay = MeasureOfDay(
                dayMilliId.toString(),
                dateMilli,
                nameMed,
                dozaMed,
                freqMed
            )

            mDayMeasureViewModel.addTimeAndMeasure(timeAndMeasure)
            mDayMeasureViewModel.addMeasure(infoDay)
            //выйти из приложения
            this.finishAffinity()
            // WorkManager.getInstance(applicationContext).cancelWorkById()
        }
    }
}