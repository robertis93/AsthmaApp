package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.InnerItemMedtimeBinding
import com.example.asthmaapp.model.MedicamentTime


class ListTimeTakeMedicamentAdapter(
    var timesMedicament: List<MedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<ListTimeTakeMedicamentAdapter.TimeTakeMedicamentViewHolder>() {

    interface OnClickListener {
        fun actionClick()
    }

    class TimeTakeMedicamentViewHolder(val binding: InnerItemMedtimeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTakeMedicamentViewHolder {
        val binding =
            InnerItemMedtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeTakeMedicamentViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TimeTakeMedicamentViewHolder, position: Int) {
        val currentItem = timesMedicament[position]

        holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(currentItem.hour)
        holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(currentItem.minute)
        holder.itemView.setOnClickListener {
            onClickListener.actionClick()
        }
    }

    override fun getItemCount(): Int {
        return timesMedicament.size
    }
}