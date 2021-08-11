package com.rightname.asthmaapp.view.fragments

import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.rightname.asthmaapp.databinding.AlarmFragmentBinding
import com.rightname.asthmaapp.model.Alarm
import com.rightname.asthmaapp.utils.NotificationsHelper
import com.rightname.asthmaapp.view.adapters.AlarmAdapter
import com.rightname.asthmaapp.view.adapters.AlarmAdapter.OnAlarmClickListener
import com.rightname.asthmaapp.viewmodel.viewModels.AlarmViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmFragment : BaseFragment<AlarmFragmentBinding>() {
    private val alarmViewModel: AlarmViewModel by lazy {
        ViewModelProvider(this).get(AlarmViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): AlarmFragmentBinding =
        AlarmFragmentBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmMeasureDeleteClickListener: OnAlarmClickListener =
            object : OnAlarmClickListener {
                override fun onDeleteAlarmClick(alarm: Alarm, position: Int) {
                    alarmViewModel.deleteAlarm(alarm)
                    WorkManager.getInstance(requireContext())
                        .cancelWorkById(UUID.fromString(alarm.id))
                }
            }

        val measureAlarmAdapter =
            AlarmAdapter(alarmMeasureDeleteClickListener)
        val measureRecyclerView = binding.recyclerAlarm
        measureRecyclerView.adapter = measureAlarmAdapter
        measureRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        val takeMedicamentAlarmAdapter =
            AlarmAdapter(alarmMeasureDeleteClickListener)
        val medicamentRecyclerView = binding.medicamentRecyclerView
        medicamentRecyclerView.adapter = takeMedicamentAlarmAdapter
        medicamentRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        alarmViewModel.alarmMeasureListLiveData.observe(viewLifecycleOwner) { alarmMeasure ->
            measureAlarmAdapter.setData(alarmMeasure)
        }

        alarmViewModel.alarmMedicamentListLiveData.observe(viewLifecycleOwner) { alarmMedicament ->
            takeMedicamentAlarmAdapter.setData(alarmMedicament)
        }

        binding.floatingActionBtnAlarmMakeMeasure.setOnClickListener {
            addAlarm(TypeAlarm.MEASURE)
        }

        binding.floatingActionBtnAlarmTakeMedicament.setOnClickListener {
            addAlarm(TypeAlarm.MEDICAMENT)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addAlarm(alarmType: TypeAlarm) {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val hourAlarm = timePicker.hour
            val minuteAlarm = timePicker.minute
            val id = setAlarm(hourAlarm, minuteAlarm, alarmType)
            if (alarmType == TypeAlarm.MEASURE) {
                if (alarmViewModel.checkMeasureAlarm(hour, minute) == false) {
                    alarmViewModel.addAlarm(id, hour, minute, alarmType)
                }
            } else {
                if (alarmViewModel.checkMedicamentAlarm(hour, minute) == false) {
                    alarmViewModel.addAlarm(id, hour, minute, alarmType)
                }
            }
        }
        showTimePicker(timeSetListener, calendar)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showTimePicker(
        timeSetListener: TimePickerDialog.OnTimeSetListener,
        calendar: Calendar
    ) {
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setAlarm(hour: Int, minute: Int, message: TypeAlarm): String {
        val now = LocalDateTime.now()
        var alarmTime =
            LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(hour, minute))
        if (alarmTime.isBefore(now))
            alarmTime = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.of(hour, minute))
        val workRequest = PeriodicWorkRequest.Builder(ScheduleJob::class.java, 1, TimeUnit.DAYS)
            .setInputData(
                workDataOf(
                    "message" to message.toString(),
                )
            )
            .setInitialDelay(Duration.between(now, alarmTime).seconds, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(requireContext())
            .enqueue(workRequest)
        return workRequest.id.toString()
    }
}

class ScheduleJob(val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        NotificationsHelper.showNotification(context, inputData.getString("message") ?: "")
        return Result.success()
    }
}

enum class TypeAlarm {
    MEASURE, MEDICAMENT
}






