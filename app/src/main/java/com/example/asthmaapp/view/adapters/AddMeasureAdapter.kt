package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMeasureItemBinding
import com.example.asthmaapp.model.TimeAndMeasure

class AddMeasureAdapter(val measuresList: List<TimeAndMeasure>, val listener: Listener) : RecyclerView.Adapter<AddMeasureAdapter.MyViewHolder>() {
    interface Listener{
        fun onDeleteClick(timeAndMeasure: TimeAndMeasure)
    }
    class MyViewHolder(val binding: AddFragmentMeasureItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AddFragmentMeasureItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measuresList[position]
        with(holder.binding) {
            timeMeasureText.text = "${com.example.asthmaapp.utils.timeConvert(currentItem.hour)} : ${
                com.example.asthmaapp.utils.timeConvert(currentItem.minute)
            }"
            addMeasureText.text = currentItem.measure.toString()
            deleteAlarmIcon.setOnClickListener {
                listener.onDeleteClick(measuresList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresList.size
    }
}