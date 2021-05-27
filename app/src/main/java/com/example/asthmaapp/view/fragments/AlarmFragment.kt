package com.example.asthmaapp.view.fragments

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.asthmaapp.viewmodel.viewModels.AlarmDrugsViewModel
import com.example.asthmaapp.viewmodel.viewModels.AlarmViewModel


class AlarmFragment : Fragment() {

    private lateinit var mAlarmViewModel: AlarmViewModel
    private lateinit var mAlarmDrugsViewModel: AlarmDrugsViewModel
    var alarmAdapter: AlarmAdapter? = null

//    companion object {
//        fun newInstance() = AlarmFragment()
//    }

    lateinit var binding: AlarmFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

// определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке
        val alarmClickListener: OnAlarmClickListener =
            object : OnAlarmClickListener {
                override fun onAlarmClick(alarm: Alarm, position: Int) {
                    mAlarmViewModel.deleteAlarm(alarm)
                    Log.v("myLogs", "AlarmFragment val alarmClickLicstener")

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

        binding.floatingActionBtnAlarm.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                // binding.root.item_txt?.text = SimpleDateFormat("HH:mm").format(cal.time)
                var hourAlarm = timePicker.hour
                var minuteAlarm = timePicker.hour
                val alarm = Alarm(0, hour, minute)
                mAlarmViewModel.addAlarm(alarm)
                //        val firstTime = binding.editTest.text.toString().toInt()
//
//        val alarm = Alarm(0, 100, firstTime)
//        mAlarmViewModel.addAlarm(alarm)
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
            val timePicker = TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.floatingActionBtnAlarm.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                // binding.root.item_txt?.text = SimpleDateFormat("HH:mm").format(cal.time)
                //var hourAlarm = timePicker.hour
                //var minuteAlarm = timePicker.hour
                val alarm = Alarm(0, hour, minute)
                mAlarmViewModel.addAlarm(alarm)
            }

            val timePicker = TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.floatingActionBtnDrugs.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val alarmDrugs = DrugsAlarm(0, hour, minute)
                mAlarmDrugsViewModel.addAlarm(alarmDrugs)

            }
            val timePicker = TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

    }

}




