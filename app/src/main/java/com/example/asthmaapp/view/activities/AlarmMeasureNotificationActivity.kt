package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.databinding.AlarmMeasureNotificationActivityBinding
import com.example.asthmaapp.viewmodel.viewModels.MeasurementsPerDayViewModel
import java.text.SimpleDateFormat
import java.util.*


class AlarmMeasureNotificationActivity : AppCompatActivity() {

    private lateinit var binding: AlarmMeasureNotificationActivityBinding
    private val measurementsPerDayViewModel: MeasurementsPerDayViewModel by lazy {
        ViewModelProvider(this).get(MeasurementsPerDayViewModel::class.java)
    }

    private lateinit var nameMedicament: String
    private lateinit var frequencyMedicament: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlarmMeasureNotificationActivityBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val currentDayMilliseconds = dateCalendar.time.time
        val currentYear = dateCalendar.time.year
        val currentMonth = dateCalendar.time.month
        val currentDayOfMonth = dateCalendar.time.day

        val dateFormatTimeHourAndMinute = SimpleDateFormat("HH:mm")
        val dateFormatTimeMinute = SimpleDateFormat("mm")
        val dateFormatTimeHour = SimpleDateFormat("HH")
        val currentTimeFormatDate = Date(currentDayMilliseconds)
        val currentTime = dateFormatTimeHourAndMinute.format(currentTimeFormatDate)

        val currentDate = Date(currentDayMilliseconds)
        val dateFormat = SimpleDateFormat("dd MMM YYYY")
        val currentDay = dateFormat.format(currentDate)

        val dateDayCalendar: Calendar =
            GregorianCalendar(currentYear, currentMonth, currentDayOfMonth)
        val dayMillisecondsId = dateDayCalendar.time.time
        binding.dateTextView.text = currentDay
        binding.timeAlarmText.text = currentTime

        measurementsPerDayViewModel.getAllMedicamentInfo.observe(
            this,
            { listMedicament ->
                nameMedicament = listMedicament.last().name
                frequencyMedicament = listMedicament.last().dose.toString()
            }
        )

        binding.saveBtn.setOnClickListener {
            val timeHour = dateFormatTimeHour.format(currentTimeFormatDate)
            val timeMinute = dateFormatTimeMinute.format(currentTimeFormatDate)
            binding.addMeasureText.setFocusableInTouchMode(true);
            binding.addMeasureText.requestFocus();
            // TODO: Peakflowmeter or PeakFlowMeter
            val measurePicflometr = binding.addMeasureText.text.toString().toInt()

//            val timeAndMeasure =
//                TimeMakeMeasure(
//                    0,
//                    dayMillisecondsId,
//                    timeHour.toInt(),
//                    timeMinute.toInt(),
//                    measurePicflometr
//                )

//            val measureOfDay = MeasureOfDay(
//                dayMillisecondsId.toString(),
//                currentDayMilliseconds,
//                nameMedicament,
//                frequencyMedicament.toInt()
//            )

          //  dayMeasureViewModel.addTimeAndMeasure(timeAndMeasure)
         //   dayMeasureViewModel.addMeasure(measureOfDay)
            this.finishAffinity()
        }
    }
}