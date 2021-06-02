package com.example.asthmaapp.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.ItemAlarmBinding
import com.example.asthmaapp.model.models.DrugsAlarm
import com.example.asthmaapp.viewmodel.viewModels.AlarmDrugsViewModel

class AlarmDrugsAdapter(var onClickAlarmDrugsListener: OnAlarmDrugsClickListener) : RecyclerView.Adapter<AlarmDrugsAdapter.AlarmViewHolder>() {
    private var alarmsList = emptyList<DrugsAlarm>()

    // определили интерфейс слушателя события нажатия
    interface OnAlarmDrugsClickListener {
        fun onAlarmClick(drugsAlarm: DrugsAlarm, position: Int)
    }

    //var onClickAlarmListener: OnAlarmClickListener? = null

//    var alarmList = mutableListOf<Alarm>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    private lateinit var mAlarmDrugsViewModel: AlarmDrugsViewModel


    class AlarmViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
//внутренний класс ViewHolder описывает элементы представления списка и привязку их к RecyclerView
    }

    //создает ViewHolder и инициализирует views для списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    //связывает views с содержимым
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        Log.v("myLogs","AlarmAdapter onBindViewHolder")

        val currentItem = alarmsList[position]
        //holder.timeTextView?.text = alarmes[position]
        holder.binding.itemTxt.text = "${currentItem.hour.toString()} : ${com.example.asthmaapp.utils.minuteShow(currentItem.minute)}"
        holder.binding.imageDeleteAlarm.setOnClickListener {
            // вызываем метод слушателя, передавая ему данные
            Log.v("myLogs","AlarmAdapter setOnClickListener")
            onClickAlarmDrugsListener?.onAlarmClick(currentItem, position)
        }
//        holder.binding.imageDeleteAlarm.setOnClickListener {
//            alarmsList.removeAt(position)
//            notifyDataSetChanged()
//        }

    }

    override fun getItemCount(): Int {
        return alarmsList.size
    }


    //передаем данные и оповещаем адаптер о необходимости обновления списка
    fun refreshDrugAlarms(drugsAlarm: List<DrugsAlarm>) {
        this.alarmsList = drugsAlarm
        //указывает адаптеру, что полученные ранее данные изменились и следует перерисовать список на экране
        notifyDataSetChanged()
    }


}
