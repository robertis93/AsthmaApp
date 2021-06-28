package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.InnerItemMedtimeBinding
import com.example.asthmaapp.model.MedicamentTime


class InnerMedTimeAdapter(
    var timesMedicament: List<MedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<InnerMedTimeAdapter.MyViewHolder>() {

    interface OnClickListener {
        fun actionClick()
    }

    class MyViewHolder(val binding: InnerItemMedtimeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            InnerItemMedtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = timesMedicament[position]

        holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(currentItem.hour)
        holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(currentItem.minute)
        holder.itemView.setOnClickListener {
            onClickListener?.actionClick()
        }
    }

    override fun getItemCount(): Int {
        return timesMedicament.size
    }
}