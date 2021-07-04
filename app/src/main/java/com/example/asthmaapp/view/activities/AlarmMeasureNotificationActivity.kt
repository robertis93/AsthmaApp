package com.example.asthmaapp.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.databinding.AlarmMeasureNotificationActivityBinding
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.models.MedicalInfo
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel
import java.text.SimpleDateFormat
import java.util.*


class AlarmMeasureNotificationActivity : AppCompatActivity() {

    private lateinit var binding: AlarmMeasureNotificationActivityBinding
    private lateinit var medicamentList: List<MedicalInfo>
    private lateinit var mMedicalViewModel: MedicalViewModel
    private lateinit var mDayMeasureViewModel: MeasureOfDayViewModel
    private var medicalInfo = mutableListOf<MedicalInfo>()
    private lateinit var nameMedicament: String
    private lateinit var dozaMedicamnet : String
    private lateinit var frequencyMedicament: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlarmMeasureNotificationActivityBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

        supportActionBar?.hide()
        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
        mDayMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)

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
        binding.timeAlarmText.setText(ttime)

        //format date
        val currentDate = Date(dateMilli)
        val dateFormat = SimpleDateFormat("dd MMM YYYY")
        val ddate = dateFormat.format(currentDate)

        val dateDayCalendar: Calendar = GregorianCalendar(year, month, day)
        val dayMilliId = dateDayCalendar.time.time
        // binding.textDate.setText(dayMilli.toString())
        binding.dateTextView.setText(ddate)

        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
        //заполняем поля Edit последними значениями из базы данных чтобы пользователь видел, что он принимает
        mMedicalViewModel.readAllData.observe(this, androidx.lifecycle.Observer { listMedicament ->
            nameMedicament = listMedicament.last().nameOfMedicine
            dozaMedicamnet = listMedicament.last().frequencyMedicine.toString()
            frequencyMedicament = listMedicament.last().doseMedicine.toString()
        })

        binding.saveBtn.setOnClickListener {
            val timeHour = dateFormatTimeHour.format(currentTime)
            val timeMinute = dateFormatTimeMinute.format(currentTime)
            binding.addMeasureText.setFocusableInTouchMode(true);
            binding.addMeasureText.requestFocus();
            val measurePicf = binding.addMeasureText.text.toString().toInt()
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

            val infoDay = MeasureOfDay(
                dayMilliId.toString(),
                dateMilli,
                nameMedicament,
                dozaMedicamnet.toInt(),
                frequencyMedicament.toInt()
            )

            mDayMeasureViewModel.addTimeAndMeasure(timeAndMeasure)
            mDayMeasureViewModel.addMeasure(infoDay)
            //выйти из приложения
            this.finishAffinity()
        }
    }
}