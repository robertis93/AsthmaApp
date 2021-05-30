package com.example.asthmaapp.view.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.databinding.AlarmFragmentBinding
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.model.models.DrugsAlarm
import com.example.asthmaapp.view.adapters.AlarmAdapter
import com.example.asthmaapp.view.adapters.AlarmAdapter.OnAlarmClickListener
import com.example.asthmaapp.view.adapters.AlarmDrugsAdapter
import com.example.asthmaapp.viewmodel.AstmaBroadcastReceiver
import com.example.asthmaapp.viewmodel.viewModels.AlarmDrugsViewModel
import com.example.asthmaapp.viewmodel.viewModels.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextInt


class AlarmFragment : Fragment() {

    private lateinit var mAlarmViewModel: AlarmViewModel
    private lateinit var mAlarmDrugsViewModel: AlarmDrugsViewModel
    var alarmAdapter: AlarmAdapter? = null
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

//    companion object {
//        fun newInstance() = AlarmFragment()
//    }

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
            object : OnAlarmClickListener {
                override fun onAlarmClick(alarm: Alarm, position: Int) {
                    mAlarmViewModel.deleteAlarm(alarm)
                    Log.v("myLogs", "AlarmFragment mAlarmViewModel.delete(alarm)")

                    Toast.makeText(context, "Hi there! This is a Toast.", Toast.LENGTH_LONG).show()
                }
            }

        //recycler
        val adapter = AlarmAdapter(alarmClickListener)
        Log.v("myLogs", "AlarmFragment val adapter = AlarmAdapter(alarmClickListener) ")

        val recyclerView = binding.recyclerAlarm
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        val drugsRecyclerView = binding.recyclerDrugs
        drugsRecyclerView.adapter = drugsAdapter
        drugsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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

                var id = Math.random().toInt()
                var hourAlarm = timePicker.hour
                var minuteAlarm = timePicker.minute
                val alarm = Alarm(id, hour, minute)
                mAlarmViewModel.addAlarm(alarm)
                Log.v("myLogs", "AlarmFragment mAlarmViewModel.addAlarm(alarm) ")
                //A PendingIntent указывает действие, которое необходимо предпринять в будущем.
                //С помощью AlarmManager вы можете запланировать выполнение кода в будущем.
                alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmIntent = Intent(context, AstmaBroadcastReceiver::class.java).let { intent ->
                    PendingIntent.getBroadcast(
                        context, id, intent, FLAG_UPDATE_CURRENT
                    )
                }

                val calendar: java.util.Calendar = java.util.Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(java.util.Calendar.HOUR_OF_DAY, hourAlarm)
                    set(java.util.Calendar.MINUTE, minuteAlarm)

                }

                alarmMgr?.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmIntent
                )
                Log.v("myLogs", "AlarmFragment alarmMgr")
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
//
//        binding.floatingActionBtnAlarm.setOnClickListener {
//            val cal = Calendar.getInstance()
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
//                cal.set(Calendar.HOUR_OF_DAY, hour)
//                cal.set(Calendar.MINUTE, minute)
//                // binding.root.item_txt?.text = SimpleDateFormat("HH:mm").format(cal.time)
//                //var hourAlarm = timePicker.hour
//                //var minuteAlarm = timePicker.hour
//                val alarm = Alarm(0, hour, minute)
//                mAlarmViewModel.addAlarm(alarm)
//            }
//
//            val timePicker = TimePickerDialog(
//                context,
//                timeSetListener,
//                cal.get(Calendar.HOUR_OF_DAY),
//                cal.get(Calendar.MINUTE),
//                true
//            ).show()
//        }

        //оповещение о принятие лекарства, все аналогично замеру
        binding.floatingActionBtnDrugs.setOnClickListener {
            Log.v("myLogs", "AlarmFragment  binding.floatingActionBtnDrugs.setOnClickListener ")
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                var idDrugs = Math.random().toInt()
                var hourAlarmDrugs = timePicker.hour
                var minuteAlarmDrugs = timePicker.minute
                val alarmDrugs = DrugsAlarm(idDrugs, hourAlarmDrugs, minuteAlarmDrugs)
                mAlarmDrugsViewModel.addAlarm(alarmDrugs)

                alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmIntent = Intent(context, AstmaBroadcastReceiver::class.java).let { intent ->
                    PendingIntent.getBroadcast(
                        context, idDrugs, intent, FLAG_UPDATE_CURRENT
                    )
                }

                val calendar: java.util.Calendar = java.util.Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(java.util.Calendar.HOUR_OF_DAY, hourAlarmDrugs)
                    set(java.util.Calendar.MINUTE, minuteAlarmDrugs)

                }

                alarmMgr?.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmIntent
                )
                Log.v("myLogs", "AlarmFragment alarmMgr")
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
}





