package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.databinding.TimeMedActivityAlarmTestBinding
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicamentViewModel
import java.text.SimpleDateFormat
import java.util.*

class AlarmMedicamentNotificationActivity : AppCompatActivity() {

    private lateinit var binding: TimeMedActivityAlarmTestBinding
    private lateinit var nameMedicament: String
    private lateinit var doseMedicament: String
    private lateinit var frequencyMedicament: String
    private val medicamentViewModel: MedicamentViewModel by lazy {
        ViewModelProvider(this).get(MedicamentViewModel::class.java)
    }
    private val dayMeasureViewModel: MeasureOfDayViewModel by lazy {
        ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimeMedActivityAlarmTestBinding.inflate(getLayoutInflater())
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
        binding.timeAlarmText.text = currentTime

        val dateFormatDayMonthYear = SimpleDateFormat("dd MMM YYYY")
        val currentDate = dateFormatDayMonthYear.format(currentTimeFormatDate)

        val dateDayCalendar: Calendar =
            GregorianCalendar(currentYear, currentMonth, currentDayOfMonth)
        val dayMidnightMilliseconds = dateDayCalendar.time.time
        binding.dateTextView.text = currentDate

        medicamentViewModel.readAllData.observe(this, androidx.lifecycle.Observer { listMedicament ->
            nameMedicament = listMedicament.last().name
            frequencyMedicament = listMedicament.last().dose.toString()
        })

        binding.saveBtn.setOnClickListener {
            val timeHour = dateFormatTimeHour.format(currentTimeFormatDate)
            val timeMinute = dateFormatTimeMinute.format(currentTimeFormatDate)

            val medicamentTime = MedicamentTime(
                0,
                timeHour.toInt(),
                timeMinute.toInt(),
                currentDayMilliseconds,
                dayMidnightMilliseconds.toString()
            )

            val infoDay = MeasureOfDay(
                dayMidnightMilliseconds.toString(),
                currentDayMilliseconds,
                nameMedicament,
                frequencyMedicament.toInt()
            )

            dayMeasureViewModel.addMedicalTime(medicamentTime)
            dayMeasureViewModel.addMeasure(infoDay)
            this.finishAffinity()
        }
    }
}