package com.example.asthmaapp.view.fragments

import android.app.*
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.asthmaapp.databinding.AlarmFragmentBinding
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.DrugsAlarm
import com.example.asthmaapp.utils.NotificationsHelper
import com.example.asthmaapp.view.adapters.MeasureAlarmAdapter
import com.example.asthmaapp.view.adapters.MeasureAlarmAdapter.OnAlarmClickListener
import com.example.asthmaapp.view.adapters.AlarmDrugsAdapter
import com.example.asthmaapp.viewmodel.viewModels.AlarmDrugsViewModel
import com.example.asthmaapp.viewmodel.viewModels.AlarmViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmFragment : Fragment() {
    private lateinit var mAlarmViewModel: AlarmViewModel
    private lateinit var mAlarmDrugsViewModel: AlarmDrugsViewModel
    lateinit var binding: AlarmFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)
//        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        mAlarmDrugsViewModel = ViewModelProvider(this).get(AlarmDrugsViewModel::class.java)

        val alarmMeasureDeleteClickListener: OnAlarmClickListener =
            object : OnAlarmClickListener {
                override fun onDeleteAlarmClick(alarm: Alarm, position: Int) {
                    mAlarmViewModel.deleteAlarm(alarm)
                    WorkManager.getInstance(requireContext())
                        .cancelWorkById(UUID.fromString(alarm.id))
                }
            }

        val alarmMedicamentDeleteClickListener: AlarmDrugsAdapter.OnAlarmDrugsClickListener =
            object : AlarmDrugsAdapter.OnAlarmDrugsClickListener {
                override fun onAlarmClick(drugsAlarm: DrugsAlarm, position: Int) {
                    mAlarmDrugsViewModel.deleteAlarm(drugsAlarm)
                    WorkManager.getInstance(requireContext())
                        .cancelWorkById(UUID.fromString(drugsAlarm.id))
                }
            }

        val measureAlarmAdapter = MeasureAlarmAdapter(alarmMeasureDeleteClickListener)
        val recyclerView = binding.recyclerAlarm
        recyclerView.adapter = measureAlarmAdapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        mAlarmViewModel.readAllData.observe(viewLifecycleOwner, Observer { alarm ->
            measureAlarmAdapter.refreshAlarms(alarm)
        })

        val drugsAdapter = AlarmDrugsAdapter(alarmMedicamentDeleteClickListener)
        val drugsRecyclerView = binding.drugsRecyclerView
        drugsRecyclerView.adapter = drugsAdapter
        drugsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        mAlarmDrugsViewModel.readAllData.observe(viewLifecycleOwner, Observer { alarm ->
            drugsAdapter.refreshDrugAlarms(alarm)
        })

        binding.floatingActionBtnAlarm.setOnClickListener {
            addMeasurementTime()
        }

        binding.floatingActionBtnDrugs.setOnClickListener {
            addTakeMedicamentTime()
        }
    }

    private fun addTakeMedicamentTime() {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val hourAlarmDrugs = timePicker.hour
            val minuteAlarmDrugs = timePicker.minute
            val idDrugs = setAlarm(hourAlarmDrugs, minuteAlarmDrugs, "Примите лекарство")
            val alarmDrugs = DrugsAlarm(idDrugs, hourAlarmDrugs, minuteAlarmDrugs)
            mAlarmDrugsViewModel.addAlarm(alarmDrugs)

        }
        showTimePicker(timeSetListener, calendar)
    }

    private fun addMeasurementTime() {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val hourAlarm = timePicker.hour
            val minuteAlarm = timePicker.minute
            val id = setAlarm(hourAlarm, minuteAlarm, "Сделайте замер")
            val alarm = Alarm(id, hour, minute)
            mAlarmViewModel.addAlarm(alarm)
        }
        showTimePicker(timeSetListener, calendar)
    }

    private fun showTimePicker(timeSetListener: TimePickerDialog.OnTimeSetListener, calendar: Calendar) {
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setAlarm(hour: Int, minute: Int, message: String): String {
        val now = LocalDateTime.now()
        var alarmTime =
            LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(hour, minute))
        if (alarmTime.isBefore(now)) // для переноса на завтра, если время уже прошло
            alarmTime = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.of(hour, minute))
        val workRequest = PeriodicWorkRequest.Builder(ScheduleJob::class.java, 1, TimeUnit.DAYS)
            .setInputData( //передача данных в запрос SheduleJob
                workDataOf(
                    //передача по ключу
                    "message" to message,
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






