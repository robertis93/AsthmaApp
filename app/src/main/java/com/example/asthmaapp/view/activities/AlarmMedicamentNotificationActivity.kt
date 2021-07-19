package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.databinding.TimeMedActivityAlarmTestBinding
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatHour
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatMinute
import com.example.asthmaapp.utils.DateUtil.dayTimeStamp
import com.example.asthmaapp.viewmodel.viewModels.MeasurementsPerDayViewModel
import java.util.*

class AlarmMedicamentNotificationActivity : AppCompatActivity() {

    private lateinit var binding: TimeMedActivityAlarmTestBinding
    private lateinit var nameMedicament: String
    private lateinit var doseMedicament: String

    private val measurementsPerDayViewModel: MeasurementsPerDayViewModel by lazy {
        ViewModelProvider(this).get(MeasurementsPerDayViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimeMedActivityAlarmTestBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val dateTimeStamp = dateCalendar.time.time
        val currentTime = timestampToDisplayTime(dateTimeStamp)

        binding.dateTextView.text = timestampToDisplayDate(dateTimeStamp)
        binding.timeAlarmText.text = currentTime

//        measurementsPerDayViewModel.getAllMedicamentInfo.observe(
//            this,
//            androidx.lifecycle.Observer { listMedicament ->
//                nameMedicament = listMedicament.last().name
//                doseMedicament = listMedicament.last().dose.toString()
//            })

        binding.saveBtn.setOnClickListener {
            val timeHour = dateTimeStampToSimpleDateFormatHour(dateTimeStamp).toInt()
            val timeMinute = dateTimeStampToSimpleDateFormatMinute(dateTimeStamp).toInt()

            val medicamentDayTimeStamp = dayTimeStamp(dateTimeStamp, timeHour, timeMinute)
            val medicamentTime =
                TakeMedicamentTimeEntity(
                    0,
                    medicamentDayTimeStamp,
                    dateTimeStamp.toString()
                )
         //   measurementsPerDayViewModel.addTakeMedicamentTime(medicamentTime)

            val medicamentInfo =
                MedicamentInfo(
                    dateTimeStamp.toString(),
                    nameMedicament,
                    doseMedicament.toInt()
                )
          //  measurementsPerDayViewModel.addMedicamentInfo(medicamentInfo)
            this.finishAffinity()
        }
    }
}