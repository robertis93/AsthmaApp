package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.InnerItemMedtimeBinding
import com.example.asthmaapp.model.TakeMedicamentTime


class ListTakeMedicamentTimeAdapter(
    var timesTimeTakeMedicament: List<TakeMedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<ListTakeMedicamentTimeAdapter.TimeTakeMedicamentViewHolder>() {

    interface OnClickListener {
        fun actionClick()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeTakeMedicamentViewHolder {
        val binding =
            InnerItemMedtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeTakeMedicamentViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TimeTakeMedicamentViewHolder, position: Int) {
        val currentItem = timesTimeTakeMedicament[position]
        holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(currentItem.takeMedicamentTime.timeHour)
        holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(currentItem.takeMedicamentTime.timeMinute)
        holder.itemView.setOnClickListener {
            onClickListener.actionClick()
        }
    }

    override fun getItemCount(): Int {
        return timesTimeTakeMedicament.size
    }

    class TimeTakeMedicamentViewHolder(val binding: InnerItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}