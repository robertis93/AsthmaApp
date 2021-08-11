package com.rightname.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.rightname.asthmaapp.databinding.TimeMedActivityAlarmTestBinding
import com.rightname.asthmaapp.utils.DateUtil.dayTimeStamp
import com.rightname.asthmaapp.utils.DateUtil.takeMedicamentCheckingEnableSaveButton
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayDate
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayHour
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayMinute
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayTime
import com.rightname.asthmaapp.viewmodel.viewModels.NotificationsViewModel
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
        setContentView(binding.root)

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        val dateTimeStamp = dateCalendar.time.time
        val currentTime = timestampToDisplayTime(dateTimeStamp)

        binding.dateTextView.text = timestampToDisplayDate(dateTimeStamp)
        binding.timeAlarmText.text = currentTime
        takeMedicamentCheckingEnableSaveButton(
            binding.editTextMedicamentName,
            binding.editTextMedicamentDose,
            binding.saveBtn
        )

        lifecycleScope.launch {
            val medicamentInfo = viewModel.getInitMedicamentInfo()
            if (medicamentInfo != null) {
                binding.saveBtn.isEnabled = true
                nameMedicament = medicamentInfo.name
                doseMedicament = medicamentInfo.dose.toString()
            } else {
                binding.editTextMedicalDoseLayout.visibility = View.VISIBLE
                binding.editTextMedicamentDose.visibility = View.VISIBLE
                binding.editTextMedicamentName.visibility = View.VISIBLE
                binding.nameMedicamentLayout.visibility = View.VISIBLE

                binding.editTextMedicamentName.doAfterTextChanged {
                    nameMedicament = it.toString()
                }
                binding.editTextMedicamentDose.doAfterTextChanged {
                    doseMedicament = it.toString()
                }
            }
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
