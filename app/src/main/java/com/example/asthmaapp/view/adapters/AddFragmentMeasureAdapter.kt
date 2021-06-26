package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMeasureItemBinding
import com.example.asthmaapp.model.TimeAndMeasure

class AddFragmentMeasureAdapter() : RecyclerView.Adapter<AddFragmentMeasureAdapter.MyViewHolder>() {
    private var measuresList = mutableListOf<TimeAndMeasure>()

    class MyViewHolder(val binding: AddFragmentMeasureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

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
            timeMeasure.text = "${com.example.asthmaapp.utils.timeConvert(currentItem.hour)} : ${
                com.example.asthmaapp.utils.timeConvert(currentItem.minute)
            }"
            measure.text = currentItem.measure.toString()
            deleteAlarm.setOnClickListener {
                deleteData(measuresList[position])
            }

        }

    }

    override fun getItemCount(): Int {
        return measuresList.size
    }

    fun addData(timeAndMeasure: TimeAndMeasure) {
        // this.measuresMedList
        measuresList.add(timeAndMeasure)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<TimeAndMeasure> {
        return measuresList
    }

    fun deleteData(timeAndMeasure: TimeAndMeasure) {
        // this.measuresMedList
        measuresList.remove(timeAndMeasure)
        notifyDataSetChanged()
    }
}