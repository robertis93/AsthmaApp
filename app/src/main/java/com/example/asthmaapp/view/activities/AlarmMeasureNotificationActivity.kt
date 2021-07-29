package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.databinding.AlarmMeasureNotificationActivityBinding
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime
import com.example.asthmaapp.viewmodel.viewModels.NotificationsViewModel
import java.util.*

class AlarmMeasureNotificationActivity : AppCompatActivity() {
    private lateinit var binding: AlarmMeasureNotificationActivityBinding
    private val viewModel: NotificationsViewModel by lazy {
        ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlarmMeasureNotificationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        val dateTimestamp = dateCalendar.time.time
        val currentTime = timestampToDisplayTime(dateTimestamp)

        binding.dateTextView.text = timestampToDisplayDate(dateTimestamp)
        binding.timeAlarmText.text = currentTime

        binding.saveBtn.setOnClickListener {
            binding.addMeasureText.isFocusableInTouchMode = true
            binding.addMeasureText.requestFocus()
            val measurePeakFlowMeter = binding.addMeasureText.text.toString().toInt()

           viewModel.addMeasure(dateTimestamp, measurePeakFlowMeter)
            this.finishAffinity()
        }
    }
}