package com.example.asthmaapp.view.fragments

import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.asthmaapp.databinding.AlarmFragmentBinding
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.utils.NotificationsHelper
import com.example.asthmaapp.view.adapters.AlarmMedicamentAdapter
import com.example.asthmaapp.view.adapters.MeasureAlarmAdapter
import com.example.asthmaapp.view.adapters.MeasureAlarmAdapter.OnAlarmClickListener
import com.example.asthmaapp.viewmodel.viewModels.AlarmViewModel
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

        val alarmMedicamentDeleteClickListener: AlarmMedicamentAdapter.OnAlarmDrugsClickListener =
            object : AlarmMedicamentAdapter.OnAlarmDrugsClickListener {
                override fun onAlarmClick(medicamentAlarm: Alarm, position: Int) {
                    alarmViewModel.deleteAlarm(medicamentAlarm)
                    WorkManager.getInstance(requireContext())
                        .cancelWorkById(UUID.fromString(medicamentAlarm.id))
                }
            }

        val measureAlarmAdapter = MeasureAlarmAdapter(alarmMeasureDeleteClickListener)
        val recyclerView = binding.recyclerAlarm
        recyclerView.adapter = measureAlarmAdapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        alarmViewModel.getListAlarm(requireContext(), TypeAlarm.MEASURE)
            ?.observe(viewLifecycleOwner, Observer { alarm ->
                measureAlarmAdapter.refreshAlarms(alarm)
            })

        val medicamentAlarmAdapter = AlarmMedicamentAdapter(alarmMedicamentDeleteClickListener)
        val drugsRecyclerView = binding.drugsRecyclerView
        drugsRecyclerView.adapter = medicamentAlarmAdapter
        drugsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        alarmViewModel.getListAlarm(requireContext(), TypeAlarm.MEDICAMENT)
            ?.observe(viewLifecycleOwner, Observer { alarm ->
                medicamentAlarmAdapter.refreshMedicamentAlarms(alarm)
            })

        binding.floatingActionBtnAlarmMakeMeasure.setOnClickListener {
            addAlarm(TypeAlarm.MEASURE)
        }

        binding.floatingActionBtnAlarmTakeMedicament.setOnClickListener {
            addAlarm(TypeAlarm.MEDICAMENT)
        }
    }

    private fun addAlarm(alarmType: TypeAlarm) {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val hourAlarm = timePicker.hour
            val minuteAlarm = timePicker.minute
            val id = setAlarm(hourAlarm, minuteAlarm, alarmType)
            val alarm = Alarm(id, hour, minute, alarmType)
            alarmViewModel.addAlarm(alarm)
        }
        showTimePicker(timeSetListener, calendar)
    }

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
        if (alarmTime.isBefore(now)) // для переноса на завтра, если время уже прошло
            alarmTime = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.of(hour, minute))
        val workRequest = PeriodicWorkRequest.Builder(ScheduleJob::class.java, 1, TimeUnit.DAYS)
            .setInputData( //передача данных в запрос SheduleJob
                workDataOf(
                    //передача по ключу
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






