package com.example.asthmaapp.view.fragments

import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.databinding.AlarmFragmentBinding
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.view.adapters.AlarmAdapter
import com.example.asthmaapp.view.adapters.ListAdapter
import com.example.asthmaapp.viewmodel.viewModels.AlarmViewModel
import kotlinx.android.synthetic.main.item_alarm.view.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmFragment : Fragment() {


    companion object {
        fun newInstance() = AlarmFragment()
    }

    lateinit var binding: AlarmFragmentBinding
    private lateinit var viewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AlarmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recycler
        val recyclerView = binding.recyclerAlarm
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AlarmAdapter(fillList())

        Log.i("adapter Alarm fragment", "myLogs")
        val a = Alarm(22, 44)
        val b = Alarm(21, 4)
        val c = Alarm(2, 19)

        binding.floatingActionBtnAlarm.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.root.item_txt.text = SimpleDateFormat("HH:mm").format(cal.time)
            }


            //создание timepickerdialog
            // timeSetListener, метод которого срабатывает при нажатии кнопки ОК на диалоге
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun fillList(): List<String> {
        val data = mutableListOf<String>()
        (0..30).forEach { i -> data.add("\$i element") }
        return data
    }


}




