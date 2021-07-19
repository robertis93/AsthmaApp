package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.databinding.AlarmMeasureNotificationActivityBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime
import com.example.asthmaapp.viewmodel.viewModels.MeasurementsPerDayViewModel
import java.util.*

class AlarmMeasureNotificationActivity : AppCompatActivity() {
    private lateinit var binding: AlarmMeasureNotificationActivityBinding
    private val measurementsPerDayViewModel: MeasurementsPerDayViewModel by lazy {
        ViewModelProvider(this).get(MeasurementsPerDayViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlarmMeasureNotificationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val dateTimeStamp = dateCalendar.time.time
        val currentTime = timestampToDisplayTime(dateTimeStamp)

        binding.dateTextView.text = timestampToDisplayDate(dateTimeStamp)
        binding.timeAlarmText.text = currentTime

        binding.saveBtn.setOnClickListener {
            binding.addMeasureText.isFocusableInTouchMode = true
            binding.addMeasureText.requestFocus()
            val measurePeakFlowMeter = binding.addMeasureText.text.toString().toInt()
            val measure = Measure(
                0,
                dateTimeStamp,
                measurePeakFlowMeter
            )
           // measurementsPerDayViewModel.addMeasure(measure)
            this.finishAffinity()
        }
    }
}