package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.ItemAlarmBinding
import com.example.asthmaapp.model.Alarm
import com.example.asthmaapp.utils.DateUtil.timeCorrectDisplay

class MeasureAlarmAdapter(
    var alarmsList: List<Alarm>,
    var onClickAlarmListener: OnAlarmClickListener
) :
    RecyclerView.Adapter<MeasureAlarmAdapter.AlarmViewHolder>() {

    interface OnAlarmClickListener {
        fun onDeleteAlarmClick(alarm: Alarm, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentItem = alarmsList[position]
        holder.binding.timeDayTextView.text =
            "${timeCorrectDisplay(currentItem.hour)} : ${timeCorrectDisplay(currentItem.minute)}"
        holder.binding.deleteIcon.setOnClickListener {
            onClickAlarmListener?.onDeleteAlarmClick(currentItem, position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return alarmsList.size
    }

    class AlarmViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)
}
