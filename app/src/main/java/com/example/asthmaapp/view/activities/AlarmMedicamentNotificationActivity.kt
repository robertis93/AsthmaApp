package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.asthmaapp.databinding.TimeMedActivityAlarmTestBinding
import com.example.asthmaapp.utils.DateUtil.dayTimeStamp
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayHour
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayMinute
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime
import com.example.asthmaapp.viewmodel.viewModels.NotificationsViewModel
import kotlinx.coroutines.launch
import java.util.*

class AlarmMedicamentNotificationActivity : AppCompatActivity() {
    private lateinit var binding: TimeMedActivityAlarmTestBinding
    private lateinit var nameMedicament: String
    private lateinit var doseMedicament: String

    private val viewModel: NotificationsViewModel by lazy {
        ViewModelProvider(this).get(NotificationsViewModel::class.java)
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

        lifecycleScope.launch {
            val medicamentInfo = viewModel.getInitMedicamentInfo()
            nameMedicament = medicamentInfo?.name.toString()
            doseMedicament = medicamentInfo?.dose.toString()
        }

        binding.saveBtn.setOnClickListener {
            val timeHour = timestampToDisplayHour(dateTimeStamp).toInt()
            val timeMinute = timestampToDisplayMinute(dateTimeStamp).toInt()
            val medicamentDayTimeStamp = dayTimeStamp(dateTimeStamp, timeHour, timeMinute)

            viewModel.addTakeMedicamentTime(medicamentDayTimeStamp, dateTimeStamp)
            viewModel.addTakeMedicament(dateTimeStamp, nameMedicament, doseMedicament)
            this.finishAffinity()
        }
    }
}