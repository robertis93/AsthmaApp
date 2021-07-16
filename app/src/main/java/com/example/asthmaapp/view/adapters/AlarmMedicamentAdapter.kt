package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.ItemAlarmBinding
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.utils.DateUtil.timeCorrectDisplay

class AlarmMedicamentAdapter(var onClickAlarmDrugsListener: OnAlarmDrugsClickListener) :
    RecyclerView.Adapter<AlarmMedicamentAdapter.AlarmViewHolder>() {
    private var alarmMedicamentList = emptyList<Alarm>()

    interface OnAlarmDrugsClickListener {
        fun onAlarmClick(medicamentAlarm: Alarm, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentItem = alarmMedicamentList[position]
        holder.binding.timeDayTextView.text =
            "${timeCorrectDisplay(currentItem.hour)} : ${timeCorrectDisplay(currentItem.minute)}"
        holder.binding.deleteIcon.setOnClickListener {
            onClickAlarmDrugsListener.onAlarmClick(currentItem, position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return alarmMedicamentList.size
    }

    fun refreshMedicamentAlarms(medicamentAlarm: List<Alarm>) {
        this.alarmMedicamentList = medicamentAlarm
        notifyDataSetChanged()
    }

    class AlarmViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)
}
