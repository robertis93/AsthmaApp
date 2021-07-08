package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMeasureItemBinding
import com.example.asthmaapp.model.TimeAndMeasure

class AddMeasureAdapter() : RecyclerView.Adapter<AddMeasureAdapter.AddMeasureViewHolder>() {
    private var measuresList = mutableListOf<TimeAndMeasure>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMeasureViewHolder {
        val binding = AddFragmentMeasureItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddMeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: AddMeasureViewHolder, position: Int) {
        val currentItem = measuresList[position]
        with(holder.binding) {
            timeMeasureText.text =
                "${com.example.asthmaapp.utils.timeConvert(currentItem.hour)} : ${
                    com.example.asthmaapp.utils.timeConvert(currentItem.minute)
                }"
            addMeasureText.text = currentItem.measure.toString()
            deleteAlarmIcon.setOnClickListener {
                deleteData(measuresList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresList.size
    }

    fun addData(timeAndMeasure: TimeAndMeasure) {
        measuresList.add(timeAndMeasure)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<TimeAndMeasure> {
        return measuresList
    }

    private fun deleteData(timeAndMeasure: TimeAndMeasure) {
        measuresList.remove(timeAndMeasure)
        notifyDataSetChanged()
    }

    class AddMeasureViewHolder(val binding: AddFragmentMeasureItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}