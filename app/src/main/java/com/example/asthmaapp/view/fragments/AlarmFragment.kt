package com.example.asthmaapp.view.fragments

import android.app.*
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.asthmaapp.view.adapters.AlarmAdapter
import com.example.asthmaapp.view.adapters.AlarmAdapter.OnAlarmClickListener
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
    ): View? {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("myLogs", "AlarmFragment OnViewCreated")

        //удаление времени при нажатии кнопки удалить
// определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке
        val alarmClickListener: OnAlarmClickListener =
            object : OnAlarmClickListener {   //
                override fun onDeleteAlarmClick(alarm: Alarm, position: Int) {
                    mAlarmViewModel.deleteAlarm(alarm)
                    //Отмена задачи
                    WorkManager.getInstance(requireContext())
                        .cancelWorkById(UUID.fromString(alarm.id))
                    Log.v("myLogs", "AlarmFragment mAlarmViewModel.delete(alarm)")

                    Toast.makeText(context, "Alarm was deleted", Toast.LENGTH_LONG).show()
                }
            }

        //recycler
        val adapter = AlarmAdapter(alarmClickListener)
        val recyclerView = binding.recyclerAlarm
        recyclerView.adapter = adapter
       // recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        //инициализация
        mAlarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)

        //подписываем адаптер на изменения списка
        mAlarmViewModel.readAllData.observe(viewLifecycleOwner, Observer { alarm ->
            adapter.refreshAlarms(alarm)
        })
        //одписываем адаптер на изменения списка с помощью функции observe(@NonNull LifecycleOwner owner,
        // @NonNull Observer<T> observer),
        // которой на вход передается объект LifecycleOwner (текущее активити) и интерфейс Observer —
        // колбек, уведомляющий об успешном получении данных.
        // При этом вызывается метод обновления списка адаптера и ему передается обновленный список.

        //второй recyclerView для оповещения и отображения медикаментов

        //удаление времени принятия лекарства при нажатии кнопки удалить
// определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке
        val alarmDrugClickListener: AlarmDrugsAdapter.OnAlarmDrugsClickListener =
            object : AlarmDrugsAdapter.OnAlarmDrugsClickListener {
                override fun onAlarmClick(drugsAlarm: DrugsAlarm, position: Int) {
                    mAlarmDrugsViewModel.deleteAlarm(drugsAlarm)
                }

            }

        val drugsAdapter = AlarmDrugsAdapter(alarmDrugClickListener)
        val drugsRecyclerView = binding.drugsRecyclerView
        drugsRecyclerView.adapter = drugsAdapter
        drugsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        mAlarmDrugsViewModel = ViewModelProvider(this).get(AlarmDrugsViewModel::class.java)

        //подписываем адаптер на изменения списка
        mAlarmDrugsViewModel.readAllData.observe(viewLifecycleOwner, Observer { alarm ->
            drugsAdapter.refreshDrugAlarms(alarm)
        })
//устанавливаем время оповещения о замере
        binding.floatingActionBtnAlarm.setOnClickListener {
            Log.v("myLogs", "AlarmFragment binding.floatingActionBtnAlarm.setOnClickListener ")
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val hourAlarm = timePicker.hour
                val minuteAlarm = timePicker.minute
                val id = setAlarm(hourAlarm, minuteAlarm, "Сделайте замер")
                val alarm = Alarm(id, hour, minute)
                mAlarmViewModel.addAlarm(alarm)
                Log.v("myLogs", "AlarmFragment mAlarmViewModel.addAlarm(alarm) ")
            }


            //создание timepickerdialog
            // В конструкторе класса TimePickerDialog также 5 параметров:
            //
            //Context — требуется контекст приложения
            //Функция обратного вызова: timeSetListener() вызывается, когда пользователь устанавливает время со следующими параметрами:
            //int hourOfDay — хранит выбранный час дня из диалога
            //int minute — хранит выбранную минуту из диалога
            //int mHours — час, который отображается при создании диалога
            //int mMinute — минута, которая отображается при создании диалога
            //boolean mBool — если false, то формат времени 24 часа, если true — 12 часов.
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }


        //оповещение о принятие лекарства, все аналогично замеру
        binding.floatingActionBtnDrugs.setOnClickListener {
            Log.v("myLogs", "AlarmFragment  binding.floatingActionBtnDrugs.setOnClickListener ")
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val hourAlarmDrugs = timePicker.hour
                val minuteAlarmDrugs = timePicker.minute
                val idDrugs = setAlarm(hourAlarmDrugs, minuteAlarmDrugs, "Примите лекарство")
                val alarmDrugs = DrugsAlarm(idDrugs, hourAlarmDrugs, minuteAlarmDrugs)
                mAlarmDrugsViewModel.addAlarm(alarmDrugs)
            }

            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    //устанавливаем оповещение
    fun setAlarm(hour: Int, minute: Int, message: String): String {
        val now = LocalDateTime.now() // настоящее время
        // время в которое нужно запустить уведомление
        var alarmTime =
            LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(hour, minute))
        if (alarmTime.isBefore(now)) // для переноса на завтра, если время уже прошло
            alarmTime = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.of(hour, minute))

        //SheduleJob оберачиваем в WorkRequest:
        //WorkRequest позволяет нам задать условия запуска и входные параметры к задаче
        // для многократного выполнения через определенный период времени нужно использовать PeriodicWorkRequest:
        val workRequest = PeriodicWorkRequest.Builder(SheduleJob::class.java, 1, TimeUnit.DAYS)
            .setInputData( //передача данных в запрос SheduleJob
                workDataOf(
                    //передача по ключу
                    "message" to message,
                )
            )
            //настройка исходной задержки, даст нужное время исполнения
            //нужно высчитать через сколько наступит время срабатывания оповещения с момента настоящего времени
            .setInitialDelay(Duration.between(now, alarmTime).seconds, TimeUnit.SECONDS)
            .build()
        //запускаем задачу:
        //Берем WorkManager и в его метод enqueue передаем WorkRequest. После этого задача будет запущена.
        WorkManager.getInstance(requireContext())
            .enqueue(workRequest) //requireContext - если внутри фрагмента
        return workRequest.id.toString()
    }
}

class SheduleJob(val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        NotificationsHelper.showNotification(context, inputData.getString("message") ?: "")
        return Result.success()
    }
}






